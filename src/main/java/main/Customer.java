package main;

import order.Order;

import java.util.Collection;

public class Customer extends Guest {

    private Collection<Order> orderHistory;
    private int firstName;
    private int lastName;
    private int phoneNumber;
    private int email;

    public Customer(Collection<Order> orderHistory, int firstName, int lastName, int phoneNumber, int email) {
        super("");
        this.orderHistory = orderHistory;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * @param order
     */
    private void addToOrderHistory(Order order) {
        orderHistory.add(order);
    }

}