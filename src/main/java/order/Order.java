package order;

import main.CookieFirm;
import main.Customer;
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
     * Condition : The order has to be payed
     */
    public void withdraw() {
        if (this.payed && this.orderState == ORDERED)
            this.orderState = WITHDRAWN;
        else if (orderState != ORDERED)
            throw new IllegalStateException("Trying to withdraw an order that wasn't ORDERED first !");
        else
            throw new WithdrawNotPaidOrderException("Trying to withdraw an unpayed order !");
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
            CookieFirm.instance().getBankAPI().refund(guest.getBankingData(), this.getPrice());
        } else throw new IllegalStateException("Order has already been Canceled or Withdrawn and can not be canceled");
    }

    /**
     * Compute the price of the order given the store taxe and if the customer have discount
     *
     * @return the price
     */
    public double getPrice() {
        double storeTax = store.getTax();

        double sum = 0.0;
        for (OrderLine line : orderLines) {
            sum += line.getAmount() * 15.0; //line.getRecipe().getPrice();
        }
        double price2 = sum * storeTax;

        if (guest.isInLoyaltyProgram() && ((Customer) guest).canHaveDiscount()) {
            price2 *= 0.9;
        double price = orderLines.stream().mapToDouble(line -> line.getAmount() * store.getRecipePrice(line.getRecipe())).sum() * storeTax;
        if (guest.isInLoyaltyProgram() && guest.canHaveDiscount()) {
            price = (price * 0.90);
        }

        return price2;
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

    public boolean isPayed() {
        return payed;
    }

    public void setPayed() {
        this.payed = true;
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

    public OrderState getState() {
        return orderState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return payed == order.payed &&
                Objects.equals(store, order.store) &&
                Objects.equals(orderLines, order.orderLines) &&
                Objects.equals(pickUpTime, order.pickUpTime) &&
                orderState == order.orderState &&
                Objects.equals(guest, order.guest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, orderLines, pickUpTime, orderState, payed, guest);
    }
}