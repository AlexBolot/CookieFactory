package main;

import main.order.Order;

import java.util.Collection;

public class Customer extends Guest {

    Collection<Order> orderHistory;
    private int firstName;
    private int lastName;
    private int phoneNumber;
    private int email;

    /**
     * @param order
     */
    private void addToOrderHistory(Order order) {
        // TODO - implement main.Customer.addToOrderHistory
        throw new UnsupportedOperationException();
    }

}