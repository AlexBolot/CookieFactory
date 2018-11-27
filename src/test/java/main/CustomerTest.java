package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import recipe.Recipe;
import store.Store;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;
import static utils.TestUtils.getInfiniteMockKitchen;


public class CustomerTest {

    private final double delta = 0.001;
    private final TestUtils utils = new TestUtils();
    private Customer customer;
    private Store store;
    private final ArrayList<Recipe> globalRecipes = new ArrayList<>();

    private final HashMap<DayOfWeek, LocalTime> openingTimes = new HashMap<>();
    private final HashMap<DayOfWeek, LocalTime> closingTimes = new HashMap<>();

    @Before
    public void before() {

        Collection<Order> orders = new ArrayList<>();
        customer = new Customer("", "", "", "", "");


        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        // Each day the store opens 5h before now and closes 5h after now (for easier testing purposes)
        for (DayOfWeek day : DayOfWeek.values()) {
            openingTimes.put(day, LocalTime.now().minusHours(5));
            closingTimes.put(day, LocalTime.now().plusHours(5));
        }

        store = new Store("", utils.randomRecipe(), globalRecipes, openingTimes, closingTimes, 15.5, 1);

        store.setKitchen(getInfiniteMockKitchen());

    }


    @Test
    public void cannotHaveDiscountfideltyP() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3));

        order.addCookie(globalRecipes.get(1), 10);
        customer.addToLoyaltyProgram();
        customer.addToOrderHistory(order);

        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void cannotHaveDiscountNotInfideltyP() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
        order.addCookie(globalRecipes.get(1), 30);
        customer.addToOrderHistory(order);

        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void canHaveDiscountfideltyP() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
        order.addCookie(globalRecipes.get(1), 30);
        customer.addToLoyaltyProgram();
        customer.addToOrderHistory(order);

        assertTrue(customer.canHaveDiscount());
    }

    @Ignore
    @Test
    public void notHaveALowerPriceFirstPurchase() {
        customer.addToLoyaltyProgram();
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);

        double unexpected = order.getStore().getRecipePrice(globalRecipes.get(1)) * 30 * store.getTax() * 0.9;

        assertNotEquals(unexpected, customer.placeOrder(true), delta);
    }

    @Test
    public void haveALowerPriceSecondPurchase() {
        customer.addToLoyaltyProgram();
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);
        customer.placeOrder(true);

        Order order2 = new Order(store, LocalDateTime.now().plusHours(3));
        order2.addCookie(globalRecipes.get(2), 2);

        customer.setTemporaryOrder(order2);

        double expected = order.getStore().getRecipePrice(globalRecipes.get(2)) * 2 * store.getTax() * 0.9;

        assertEquals(expected, customer.placeOrder(true), delta);
    }

    @Test
    public void loseDiscountAfterSecondPurchase() {
        customer.addToLoyaltyProgram();
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
//        fillKitchenForRecipe(store.getKitchen(),globalRecipes.get(1),30);
//        fillKitchenForRecipe(store.getKitchen(),globalRecipes.get(2),3);
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);
        customer.placeOrder(true);

        Order order2 = new Order(store, LocalDateTime.now().plusHours(3));
        order2.addCookie(globalRecipes.get(2), 2);
        customer.setTemporaryOrder(order2);
        customer.placeOrder(true);

        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void recoverDiscountAfter4Purchase() {
        customer.addToLoyaltyProgram();

        //First order, no discount
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);
        assertEquals(this.store.getRecipePrice(globalRecipes.get(1)) * 30 * this.store.getTax(), customer.placeOrder(true), delta);

        //Second order, first Discount :
        Order order2 = new Order(this.store, LocalDateTime.now().plusHours(3));
        order2.addCookie(globalRecipes.get(2), 2);
        customer.setTemporaryOrder(order2);
        assertEquals(this.store.getRecipePrice(globalRecipes.get(2)) * 2 * this.store.getTax() * 0.9, customer.placeOrder(true), delta);

        //Third order, no discount
        Order order3 = new Order(this.store, LocalDateTime.now().plusHours(3));
        order3.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order3);
        assertEquals(this.store.getRecipePrice(globalRecipes.get(1)) * 30 * this.store.getTax(), customer.placeOrder(true), delta);

        //Forth order,second discount :
        Order order4 = new Order(this.store, LocalDateTime.now().plusHours(3));
        order4.addCookie(globalRecipes.get(2), 2);
        customer.setTemporaryOrder(order4);
        assertEquals(this.store.getRecipePrice(globalRecipes.get(2)) * 2 * this.store.getTax() * 0.9, customer.placeOrder(true), delta);
        assertFalse(customer.canHaveDiscount());
    }

    @Ignore
    @Test
    public void doNothaveALowerPrice() {
        Order order = new Order(store, LocalDateTime.now().plusHours(3));
        order.addCookie(globalRecipes.get(1), 30);
        customer.setTemporaryOrder(order);

        double unexpected = order.getStore().getRecipePrice(globalRecipes.get(1)) * 30 * store.getTax() * 0.9;

        assertNotEquals(unexpected, customer.placeOrder(true), delta);

    }

    @Test
    public void areTheyInFidelityProgram() {
        Guest guest = new Guest("Michel@michel.py");
        Customer loyalCustomer = new Customer("","","","","");
        loyalCustomer.addToLoyaltyProgram();
        Customer felonCustomer = new Customer("","","","","");
        Assert.assertFalse(guest.isInLoyaltyProgram());
        Assert.assertTrue(loyalCustomer.isInLoyaltyProgram());
        Assert.assertFalse(felonCustomer.isInLoyaltyProgram());

    }

}
