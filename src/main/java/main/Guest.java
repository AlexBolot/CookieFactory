package main;

import order.Order;


public class Guest {

    private Order temporaryOrder;
    private String email;
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

    public String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }
}