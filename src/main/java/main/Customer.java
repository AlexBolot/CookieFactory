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

    Customer(String firstName, String lastName, String phoneNumber, String email, String password) {
        setEmail(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.orderHistory = new ArrayList<>();
        initOrder();
    }

    Customer(String firstName, String lastName, String phoneNumber, String email, String password, Order temporaryOrder) {
        this(email, firstName, lastName, phoneNumber, password);
        this.setTemporaryOrder(temporaryOrder);
    }

    public static Customer from(Guest guest, String firstName, String lastName, String phoneNumber, String password) {
        Customer customer = new Customer(firstName, lastName, phoneNumber, guest.getEmail(), password);
        customer.setId(guest.getId());
        customer.setBankingData(guest.getBankingData());
        customer.setTemporaryOrder(guest.getTemporaryOrder());

        return customer;
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

        double price = super.placeOrder(onlinePayment);

        if (loyaltyProgram && haveDiscount) {
            useDiscount();
        }

        addToOrderHistory(order);

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

    private void useDiscount() {
        haveDiscount = false;
        cookieCount = 0;
    }

    /**
     * Add the customer to the loyalty program
     */
    void addToLoyaltyProgram() {
        loyaltyProgram = true;
    }

    public boolean canHaveDiscount() {
        return haveDiscount;
    }

    @Override
    public boolean isInLoyaltyProgram() {
        return loyaltyProgram;
    }

    public Collection<Order> getOrderHistory() {
        return orderHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (loyaltyProgram != customer.loyaltyProgram) return false;
        if (cookieCount != customer.cookieCount) return false;
        if (haveDiscount != customer.haveDiscount) return false;
        if (orderHistory != null ? !orderHistory.equals(customer.orderHistory) : customer.orderHistory != null)
            return false;
        if (!firstName.equals(customer.firstName)) return false;
        if (!lastName.equals(customer.lastName)) return false;
        if (!phoneNumber.equals(customer.phoneNumber)) return false;
        return password.equals(customer.password);
    }

    @Override
    public int hashCode() {
        int result = orderHistory != null ? orderHistory.hashCode() : 0;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + (loyaltyProgram ? 1 : 0);
        result = 31 * result + cookieCount;
        result = 31 * result + (haveDiscount ? 1 : 0);
        result = 31 * result + password.hashCode();
        return result;
    }
}