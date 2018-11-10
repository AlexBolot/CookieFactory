package main;

import order.Order;

import java.util.Collection;

public class Customer extends Guest {

    private Collection<Order> orderHistory;
    private int firstName;
    private int lastName;
    private int phoneNumber;
    private int email;
    private boolean loyaltyProgram = false;
    private int cookieCount=0;
    private boolean discount = false;

    public Customer(Collection<Order> orderHistory, int firstName, int lastName, int phoneNumber, int email) {
        super();
        this.orderHistory = orderHistory;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Add the new order to the history of the customer
     * For each new order add the number of coocki in the
     * loyalty program
     * @param order by the customer
     */
    private void addToOrderHistory(Order order) {
        if(loyaltyProgram){
            order.getOrderLines().forEach(orderLine ->
            {
                fideltyPoints(orderLine.getAmount());
            });
        }
        orderHistory.add(order);
    }

    /**
     * Add the amount of cookie to the cookie counter
     * When the cookie counter is more than 30, then
     * the customer can have a discount
     * @param amount of cookie
     */
    private void fideltyPoints(int amount) {
        if (cookieCount < 30 && !discount) {
            cookieCount += amount;
        } else {
            discount = true;
        }
    }

    /**
     * Add the customer to the loyalty program
     */
    public void addToLoyaltyP(){
        loyaltyProgram = true;
    }

    public boolean canHaveDiscount() {
        return discount;
    }
}