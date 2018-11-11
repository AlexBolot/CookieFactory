package main;

import order.Order;

import java.util.Collection;

public class Customer extends Guest {

    private Collection<Order> orderHistory;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;

    public Customer(Collection<Order> orderHistory, String firstName, String lastName, String phoneNumber, String email, String password) {
        super(email);
        this.orderHistory = orderHistory;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    /**
     * @param order
     */
    private void addToOrderHistory(Order order) {
        orderHistory.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (orderHistory != null ? !orderHistory.equals(customer.orderHistory) : customer.orderHistory != null)
            return false;
        if (lastName != null ? !lastName.equals(customer.lastName) : customer.lastName != null) return false;
        if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null) return false;
        if (password != null ? !password.equals(customer.password) : customer.password != null) return false;
        return email != null ? email.equals(customer.email) : customer.email == null;
    }
}