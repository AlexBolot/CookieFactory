package main;

import order.Order;

import java.util.ArrayList;
import java.util.Collection;

public class Customer extends Guest {

    private Collection<Order> orderHistory;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean loyaltyProgram = false;
    private int cookieCount = 0;
    private boolean haveDiscount = false;
    private String password;

    public Customer(String email, String firstName, String lastName, String phoneNumber) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.orderHistory = new ArrayList<>();
    }

    public Customer(Collection<Order> orderHistory, String firstName, String lastName, String phoneNumber, String email, String password) {
        super(email);
        this.orderHistory = orderHistory;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password, Order temporaryOrder) {
        super(email);
        this.orderHistory = orderHistory;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.setTemporaryOrder(temporaryOrder);
    }

    public Collection<Order> getOrderHistory() {
        return orderHistory;
    }

    /**
     * Check if the order has been place and return the price
     *
     * @param onlinePayment boolean if the client payed online or not
     * @return price of the order, or 0.0 if the order is not taken
     */
    @Override
    public double placeOrder(boolean onlinePayment) {

        Order order = getTemporaryOrder();

        if (order.isPayed())
            throw new IllegalStateException("The order you are trying to place has already been paid");

        order.setGuest(this);

        double price = order.getStore().placeOrder(order);

        if (onlinePayment) {
            order.setPayed();
        }

        if (loyaltyProgram && haveDiscount) {
            useDiscount();
        }

        addToOrderHistory(order);

        setTemporaryOrder(createOrder());

        return price;
    }

    /**
     * Add the new order to the history of the customer
     * For each new order add the number of coockie in the
     * loyalty program
     *
     * @param order by the customer
     */
    void addToOrderHistory(Order order) {
        if (loyaltyProgram) {
            order.getOrderLines().forEach(orderLine -> fideltyPoints(orderLine.getAmount()));
        }

        orderHistory.add(order);
    }

    /**
     * Add the amount of cookie to the cookie counter
     * When the cookie counter is more than 30, then
     * the customer can have a haveDiscount
     *
     * @param amount of cookie
     */
    private void fideltyPoints(int amount) {
        if (cookieCount < 30 && !haveDiscount) {
            cookieCount += amount;
        }
        if (cookieCount >= 30) {
            haveDiscount = true;
        }
    }

    public boolean isInLoyaltyProgram() {
        return loyaltyProgram;
    }

    /**
     * Add the customer to the loyalty program
     */
    public void addToLoyaltyProgram() {
        loyaltyProgram = true;
    }

    public boolean canHaveDiscount() {
        return haveDiscount;
    }

    public void useDiscount() {
        haveDiscount = false;
        cookieCount = 0;
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
        return getEmail() != null ? getEmail().equals(customer.getEmail()) : customer.getEmail() == null;
    }
}