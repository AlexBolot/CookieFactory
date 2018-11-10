package main;

import order.Order;


public class Guest {

    private Order temporaryOrder;
    private String email;
    public Guest(String email) {
        this.temporaryOrder = createOrder();
        this.email = email;
    }

    public Order createOrder() {
        Order voidOrder = new Order();
        this.temporaryOrder = voidOrder;
        return voidOrder;
    }

    public void placeOrder(boolean onlinePayment) {

        if (temporaryOrder.isPayed())
            throw new IllegalStateException("The order you are trying to place has already been paid");

        temporaryOrder.setGuest(this);

        if (onlinePayment) {
            temporaryOrder.setPayed();
        }

        temporaryOrder.getStore().placeOrder(temporaryOrder);
    }

    public void setTemporaryOrder(Order order) {
        this.temporaryOrder = order;
    }

    public String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method is just a mock method, that "would" send a refund request to the connected banking system
     * "In a perfect world where everything is connected" :)
     */
    public void refund() {
        // Does nothing :D
    }
}