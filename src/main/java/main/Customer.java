package main;

import order.Order;

import java.util.Collection;

public class Customer extends Guest {

    private Collection<Order> orderHistory;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public Customer(Collection<Order> orderHistory, String firstName, String lastName, String phoneNumber, String email) {
        super();
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