package main;

import order.Order;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CustomerTest {

    private TestUtils utils = new TestUtils();
    Customer customer;
    Store store;
    private ArrayList<Recipe> globalRecipes = new ArrayList<>();


    private HashMap<Day, LocalDateTime> openingTimes = new HashMap<>();
    private HashMap<Day, LocalDateTime> closingTimes = new HashMap<>();

    @Before
    public void before(){

        Collection<Order> orders = new ArrayList<>();
        customer = new Customer(orders,"","","","" );


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
    public void cannotHaveDiscountfideltyP(){
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);

        order.addCookie(globalRecipes.get(1), 10);
        customer.addToLoyaltyP();
        customer.addToOrderHistory(order);
        assertEquals(false, customer.canHaveDiscount());
    }

    @Test
    public void cannotHaveDiscountNotInfideltyP(){
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.addToOrderHistory(order);
        assertEquals(false, customer.canHaveDiscount());
    }

    @Test
    public void canHaveDiscountfideltyP(){
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.addToLoyaltyP();
        customer.addToOrderHistory(order);
        assertEquals(true, customer.canHaveDiscount());
    }

    @Test
    public void notHaveALowerPriceFirstPurchase(){
        customer.addToLoyaltyP();
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        assertTrue((globalRecipes.get(1).price * 30 * store.getTax())*0.9 != customer.placeOrder(true, order));
    }

    @Test
    public void haveALowerPriceSecondPurchase(){
        customer.addToLoyaltyP();
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.placeOrder(true, order);

        Order order2 = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order2.addCookie(globalRecipes.get(2), 2);

        assertEquals((globalRecipes.get(2).price * 2 * store.getTax())*0.9, customer.placeOrder(true, order2),0.0);
    }

    @Test
    public void loseDiscountAfterSecondPurchase(){
        customer.addToLoyaltyP();
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        customer.placeOrder(true, order);

        Order order2 = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order2.addCookie(globalRecipes.get(2), 2);
        customer.placeOrder(true, order2);
        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void recoverDiscountAfter4Purchase(){
        customer.addToLoyaltyP();

        //First order, no discount
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        assertEquals(globalRecipes.get(1).price * 30 * store.getTax(),   customer.placeOrder(true, order),0.0);

        //Second order, first Discount :
        Order order2 = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order2.addCookie(globalRecipes.get(2), 2);
        assertEquals((globalRecipes.get(2).price * 2 * store.getTax())*0.9, customer.placeOrder(true, order2),0.0);

        //Third order, no discount
        Order order3 = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order3.addCookie(globalRecipes.get(1), 30);
        assertEquals(globalRecipes.get(1).price * 30 * store.getTax(),  customer.placeOrder(true, order3),0.0);

        //Forth order,second discount :
        Order order4 = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order4.addCookie(globalRecipes.get(2), 2);
        assertEquals((globalRecipes.get(2).price * 2 * store.getTax())*0.9, customer.placeOrder(true, order4),0.0);
        assertFalse(customer.canHaveDiscount());
    }

    @Test
    public void doNothaveALowerPrice(){
        Order order = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY);
        order.addCookie(globalRecipes.get(1), 30);
        assertTrue((globalRecipes.get(1).price * 30 * store.getTax())*0.9 != customer.placeOrder(true, order));

    }

}
