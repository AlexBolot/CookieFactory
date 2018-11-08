package main;

import order.Order;

import java.util.Date;

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

    public void placeOrder() {
        temporaryOrder.getStore().placeOrder(temporaryOrder);
    }

    public void setTemporaryOrder(Order order) {
        this.temporaryOrder=order;
    }
}