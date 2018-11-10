package main;

import order.Order;

import java.util.Collection;

public class Customer extends Guest {

    private Collection<Order> orderHistory;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private boolean loyaltyProgram = false;
    private int cookieCount=0;
    private boolean haveDiscount = false;

    public Customer(Collection<Order> orderHistory, String firstName, String lastName, String phoneNumber, String email) {
        super(email);
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
    void addToOrderHistory(Order order) {
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
     * the customer can have a haveDiscount
     * @param amount of cookie
     */
    private void fideltyPoints(int amount) {
        if (cookieCount < 30 && !haveDiscount) {
            cookieCount += amount;
        }
        if (cookieCount>=30){
            haveDiscount = true;
        }
    }

    /**
     * Add the customer to the loyalty program
     */
    public void addToLoyaltyP(){
        loyaltyProgram = true;
    }


    public boolean canHaveDiscount() {
        return haveDiscount;
    }
}