package order;

import api.BankingData;
import main.CookieFirm;
import main.Guest;
import recipe.Recipe;
import store.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.min;
import static order.OrderState.*;

public class Order {

    private Store store;
    private List<OrderLine> orderLines = new ArrayList<>();
    private LocalDateTime pickUpTime;
    private OrderState orderState = DRAFT;

    private boolean payed = false;
    private boolean hadDiscount=false;
    private BankingData bankingData;
    private Guest guest;

    public Order(Store store, LocalDateTime pickUpTime) {
        this.store = store;
        this.pickUpTime = pickUpTime;
    }

    public Order() {
        this.store = null;
        this.pickUpTime = null;
    }

    /**
     * Adds a cookie to this order
     *
     * @param recipe Recipe of the cookie to add
     * @param amount Amount of cookies of [recipe] (must be strictly positive)
     * @return boolean, true if cookies where added to the list false if the store couldn't make the recipe
     */
    public boolean addCookie(Recipe recipe, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount should be a strictly positive number");

        Optional<OrderLine> orderLineOptional = orderLines.stream().filter(line -> line.getRecipe().equals(recipe)).findFirst();

        amount = min(store.getKitchen().recipeCapacity(recipe), amount);

        if (amount == 0) return false;

        if (orderLineOptional.isPresent()) {
            OrderLine orderLine = orderLineOptional.get();
            orderLine.setAmount(orderLine.getAmount() + amount);
        } else {
            OrderLine orderLine = new OrderLine(recipe, amount);
            orderLines.add(orderLine);
        }
        return true;
    }

    /**
     * Removes a cookie from this order
     *
     * @param recipe Recipe of the cookie to remove
     * @param amount Amount of cookies of [recipe] (must be strictly positive)
     */
    public void removeCookie(Recipe recipe, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount should be a strictly positive number");

        OrderLine orderLine = orderLines.stream()
                .filter(line -> line.getRecipe().equals(recipe))
                .findFirst().orElseThrow(IllegalArgumentException::new);

        orderLine.reduceAmount(amount);

        if (orderLine.getAmount() <= 0)
            orderLines.remove(orderLine);
    }

    /**
     * Changes the OrderState to WITHDRAWN
     */
    public void withdraw() {
        withdraw(0);
    }

    /**
     * Changes the OrderState to WITHDRAWN and applies given [discount]
     * If bankingData is null, payment will me made in the store by the employee -> out of out scope
     * Else, payment is made using given banking data
     *
     * @param discount Amount of money to reduce from the order's price
     */
    public void withdraw(double discount) {
        if (this.orderState == ORDERED) {
            this.orderState = WITHDRAWN;

            applyUnFaithPassBonus();

            if (bankingData != null) {
                double orderPrice = getPrice();

                if (discount < 0)
                    throw new IllegalArgumentException("Discount given is negative.");

                if (orderPrice - discount < 0)
                    throw new IllegalArgumentException("Discount greater than order's price : negative price as result");

                CookieFirm.instance().getBankAPI().pay(guest.getBankingData(), orderPrice - discount);
            }
        } else
            throw new IllegalStateException("Trying to withdraw an order that isn't in ORDERED state (maybe canceled, still in draft, or already withdrawn) !");
    }

    /**
     * Withdraw an order using an UnFaithPass
     *
     * @param points number of points used at the withdrawal of the order
     * @param freeCookie number of free cookies asked at the withdrawal of the order
     */

    public void withdraw(int points, int freeCookie) {
        if (points==0 && freeCookie==0) {withdraw(); return;}
        if (this.getGuest().getUnFaithPass().use(points,freeCookie)) {
            if (this.getStore().getUnFaithPassProgram()!=null) {
                double correspondingPrice = this.getStore().getUnFaithPassProgram().getCashFromRewardValue(points);
                withdraw(correspondingPrice);
            }
        } else {
            throw new IllegalArgumentException("The guest can't pay this amount with his actual unFaithPass");
        }
    }


    public void applyUnFaithPassBonus() {
        if (this.getGuest().getUnFaithPass()!=null) {
            this.getStore().collectRewards(this,this.getGuest().getUnFaithPass());
        }
    }

    /**
     * Changes the OrderState to ORDERED
     * Condition : The order must be in a DRAFT state
     *
     * @return The total price of the order
     */
    public double placeOrder() {
        if (this.orderState == DRAFT)
            this.orderState = ORDERED;
        else throw new IllegalStateException("Trying to place order on an order that isn't in a DRAFT state");

        return getPrice();
    }

    /**
     * Changes the OrderState to CANCELED
     * Condition : The order has to be in ORDERED state
     */
    public void cancel() {
        if (this.orderState == ORDERED) {
            this.orderState = CANCELED;
        } else throw new IllegalStateException("Order has already been Canceled or Withdrawn and can not be canceled");
    }

    /**
     * Compute the price of the order given the store taxe and if the customer have discount
     *
     * @return the price
     */
    public double getPrice() {
        double storeTax = store.getTax();

        double price = orderLines.stream().mapToDouble(line -> line.getAmount() * store.getRecipePrice(line.getRecipe())).sum() * storeTax;
        if (guest.isInLoyaltyProgram() && guest.canHaveDiscount()) {
            hadDiscount = true;
            price = (price * 0.90);
        }

        return price;
    }

    public Store getStore() {
        return this.store;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public BankingData getBankingData() {
        return bankingData;
    }

    public void setBankingData(BankingData bankingData) {
        this.bankingData = bankingData;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public LocalDateTime getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(LocalDateTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public void pay() {
        CookieFirm.instance().getBankAPI().pay(guest.getBankingData(), this.getPrice());
        this.payed = true;
    }

    public boolean didItHadDiscount(){
        return hadDiscount;
    }

    public OrderState getState() {
        return orderState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(store, order.store) &&
                Objects.equals(orderLines, order.orderLines) &&
                Objects.equals(pickUpTime, order.pickUpTime) &&
                orderState == order.orderState &&
                Objects.equals(bankingData, order.bankingData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, orderLines, pickUpTime, orderState, payed, guest.getId());
    }
}