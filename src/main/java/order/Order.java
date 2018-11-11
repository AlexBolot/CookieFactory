package order;

import main.*;
import main.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Order {

    Store store;
    Collection<OrderLine> orderLines = new ArrayList<>();
    LocalDateTime pickUpTime;
    Day pickupDay;
    OrderState orderState = OrderState.DRAFT;


    boolean payed = false;
    private Guest guest;

    public Order(Store store, LocalDateTime pickUpTime, Day pickupDay) {
        this.store = store;
        this.pickUpTime = pickUpTime;
        this.pickupDay = pickupDay;

    }

    public Order() {
        this.store=null;
        this.pickUpTime=null;
    }




    /**
     * @param recipe
     * @param amount
     */
    public void addCookie(Recipe recipe, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount should be a strictly positive number");
        Optional<OrderLine> orderLineOptional = orderLines.stream().filter(line -> line.recipe.equals(recipe)).findFirst();
        if (!orderLineOptional.isPresent()) {
            OrderLine orderLine = new OrderLine(recipe, amount);
            orderLines.add(orderLine);
        } else {
            OrderLine orderLine = orderLineOptional.get();
            orderLine.setAmount(orderLine.getAmount() + amount);
        }

    }

    /**
     * @param recipe
     * @param amount
     */
    public void removeCookie(Recipe recipe, int amount) {
        OrderLine orderLine = orderLines.stream()
                .filter(line -> line.recipe.equals(recipe))
                .findFirst().orElseThrow(IllegalArgumentException::new);
        orderLine.amount -= amount;
        if (orderLine.amount <= 0)
            orderLines.remove(orderLine);

    }

    /**
     * Compute the price of the order given the store taxe and if the customer have discount
     * @return the price
     */
    public double getPrice() {
        double storeTax = store.getTax();
        double price = orderLines.stream().mapToDouble(line -> line.amount * line.recipe.getPrice()).sum() * storeTax;
        if(guest instanceof Customer && ((Customer) guest).isInLoyaltyProgram() && ((Customer) guest).canHaveDiscount()){
            price=(price*0.90);
        }
        return price;
    }

    public Store getStore() {
        return this.store;
    }

    public OrderState getOrderState() {
        return orderState;
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

    public Collection<OrderLine> getOrderLines() {
        return orderLines;
    }

    public LocalDateTime getPickUpTime() {
        return pickUpTime;
    }

    public Day getPickupDay() {
        return pickupDay;
    }

    //TODO:maybe should dispear, as the state is an order progression, we could use different method "pay","withdraw" etc
    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void pay() {
        this.payed = true;
    }

    public OrderState getState() {
        return orderState;
    }
    public void withdraw() {
        if (this.payed)
            this.orderState = OrderState.WITHDRAWN;
        else {
            throw new WithdrawNotPaidOrderException();
        }
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