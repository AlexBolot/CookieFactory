package main;

import order.Order;

public class Guest {

    private Order temporaryOrder;

    public Guest() {
        this.temporaryOrder = createOrder();
    }

    public Order createOrder() {
        Order voidOrder = new Order();
        this.temporaryOrder = voidOrder;
        return voidOrder;
    }

    public void placeOrder(boolean onlinePayment) {

        if (temporaryOrder.isPayed())
            throw new IllegalStateException("The order you are trying to place has already been paid");

        if (onlinePayment) {
            temporaryOrder.setPayed();
        }

        temporaryOrder.getStore().placeOrder(temporaryOrder);
    }

    public void setTemporaryOrder(Order order) {
        this.temporaryOrder = order;
    }
}