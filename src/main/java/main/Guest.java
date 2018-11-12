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

    public double placeOrder(boolean onlinePayment) {

        if (temporaryOrder.isPayed())
            throw new IllegalStateException("The order you are trying to place has already been paid");

        temporaryOrder.setGuest(this);

        double price = 0.0;
        try {
            price = temporaryOrder.getStore().placeOrder(temporaryOrder);
        }
        catch (IllegalArgumentException e){

        }

        if (onlinePayment) {
            temporaryOrder.setPayed();
        }

        return price;
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