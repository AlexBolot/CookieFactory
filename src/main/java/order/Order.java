package order;

import main.Customer;
import main.Day;
import main.Guest;
import recipe.Recipe;
import store.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;
import static order.OrderState.*;

public class Order {

    private Store store;
    private List<OrderLine> orderLines = new ArrayList<>();
    private LocalDateTime pickUpTime;
    private Day pickupDay;
    private OrderState orderState = DRAFT;

    private boolean payed = false;
    private Guest guest;

    public Order(Store store, LocalDateTime pickUpTime, Day pickupDay) {
        this.store = store;
        this.pickUpTime = pickUpTime;
        this.pickupDay = pickupDay;

    }

    public Order() {
        this.store = null;
        this.pickUpTime = null;
    }

    /**
     * Adds a cookie to this order
     *  @param recipe Recipe of the cookie to add
     * @param amount Amount of cookies of [recipe] (must be strictly positive)
     * @return boolean, true if cookies where added to the list false if the store couldn't make the recipe
     */
    public boolean addCookie(Recipe recipe, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount should be a strictly positive number");

        Optional<OrderLine> orderLineOptional = orderLines.stream().filter(line -> line.getRecipe().equals(recipe)).findFirst();
        if (!store.getKitchen().canDo(recipe))
            return false;
        else
            amount = min(store.getKitchen().recipeCapacity(recipe), amount);
        if (!orderLineOptional.isPresent()) {
            OrderLine orderLine = new OrderLine(recipe, amount);
            orderLines.add(orderLine);
        } else {
            OrderLine orderLine = orderLineOptional.get();
            orderLine.setAmount(orderLine.getAmount() + amount);
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
            guest.refund();
        } else throw new IllegalStateException("Order has already been Canceled or Withdrawn and can not be canceled");
    }

    /**
     * Compute the price of the order given the store taxe and if the customer have discount
     *
     * @return the price
     */
    public double getPrice() {
        double storeTax = store.getTax();
        double price = orderLines.stream().mapToDouble(line -> line.getAmount() * line.getRecipe().getPrice()).sum() * storeTax;
        if (guest instanceof Customer && ((Customer) guest).isInLoyaltyProgram() && ((Customer) guest).canHaveDiscount()) {
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

    public Day getPickupDay() {
        return pickupDay;
    }

    public void setPickUpTime(LocalDateTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public void setPickupDay(Day pickupDay) {
        this.pickupDay = pickupDay;
    }

    public void pay() {
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

        if (pickUpTime != null ? !pickUpTime.equals(order.pickUpTime) : order.pickUpTime != null) return false;
        if (pickupDay != order.pickupDay) return false;
        return guest != null ? guest.equals(order.guest) : order.guest == null;
    }

    @Override
    public int hashCode() {
        int result = pickUpTime != null ? pickUpTime.hashCode() : 0;
        result = 31 * result + (pickupDay != null ? pickupDay.hashCode() : 0);
        result = 31 * result + (guest != null ? guest.hashCode() : 0);
        return result;
    }


}