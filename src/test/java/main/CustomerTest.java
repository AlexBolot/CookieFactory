package main;

import order.Order;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;


public class CustomerTest {

    private double delta = 0.001;
    private TestUtils utils = new TestUtils();
    private Customer customer;
    private Store store;
    private ArrayList<Recipe> globalRecipes = new ArrayList<>();

    private HashMap<Day, LocalDateTime> openingTimes = new HashMap<>();
    private HashMap<Day, LocalDateTime> closingTimes = new HashMap<>();

    @Before
    public void before() {

        Collection<Order> orders = new ArrayList<>();
        customer = new Customer(orders, "", "", "", "", "");


        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        // Each day the store opens 5h before now and closes 5h after now (for easier testing purposes)
        for (Day day : Day.values()) {
            openingTimes.put(day, LocalDateTime.now().minusHours(5));
            closingTimes.put(day, LocalDateTime.now().plusHours(5));
        }

        store = new Store(utils.randomRecipe(), globalRecipes, orders, openingTimes, closingTimes, 15.5);

    }


    @Test
    public void cannotHaveDiscountfideltyP() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);

        order.addCookie(globalRecipes.get(1), 10);
        customer.addToLoyaltyProgram();
        customer.addToOrderHistory(order);

        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void cannotHaveDiscountNotInfideltyP() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.addToOrderHistory(order);

        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void canHaveDiscountfideltyP() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.addToLoyaltyProgram();
        customer.addToOrderHistory(order);

        assertTrue(customer.canHaveDiscount());
    }

    @Test
    public void notHaveALowerPriceFirstPurchase() {
        customer.addToLoyaltyProgram();
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);

        double unexpected = globalRecipes.get(1).price * 30 * store.getTax() * 0.9;

        assertNotEquals(unexpected, customer.placeOrder(true), delta);
    }

    @Test
    public void haveALowerPriceSecondPurchase() {
        customer.addToLoyaltyProgram();
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);
        customer.placeOrder(true);

        Order order2 = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order2.addCookie(globalRecipes.get(2), 2);

        customer.setTemporaryOrder(order2);

        double expected = globalRecipes.get(2).price * 2 * store.getTax() * 0.9;

        assertEquals(expected, customer.placeOrder(true), delta);
    }

    @Test
    public void loseDiscountAfterSecondPurchase() {
        customer.addToLoyaltyProgram();
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);
        customer.placeOrder(true);

        Order order2 = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order2.addCookie(globalRecipes.get(2), 2);
        customer.setTemporaryOrder(order2);
        customer.placeOrder(true);

        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void recoverDiscountAfter4Purchase() {
        customer.addToLoyaltyProgram();

        //First order, no discount
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);
        assertEquals(globalRecipes.get(1).price * 30 * store.getTax(), customer.placeOrder(true), delta);

        //Second order, first Discount :
        Order order2 = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order2.addCookie(globalRecipes.get(2), 2);
        customer.setTemporaryOrder(order2);
        assertEquals(globalRecipes.get(2).price * 2 * store.getTax() * 0.9, customer.placeOrder(true), delta);

        //Third order, no discount
        Order order3 = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order3.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order3);
        assertEquals(globalRecipes.get(1).price * 30 * store.getTax(), customer.placeOrder(true), delta);

        //Forth order,second discount :
        Order order4 = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order4.addCookie(globalRecipes.get(2), 2);
        customer.setTemporaryOrder(order4);
        assertEquals(globalRecipes.get(2).price * 2 * store.getTax() * 0.9, customer.placeOrder(true), delta);
        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void doNothaveALowerPrice() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);

        double unexpected = globalRecipes.get(1).price * 30 * store.getTax() * 0.9;

        assertNotEquals(unexpected, customer.placeOrder(true), delta);

    }

}
