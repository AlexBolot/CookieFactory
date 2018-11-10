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
        customer = new Customer(orders,1,1,1,1 );


        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        int startDay = 5;

        for (Day day : Day.values()) {

            int openHour = new Random().nextInt(2) + 7;
            int openMin = new Random().nextInt(60);
            int closeHour = new Random().nextInt(2) + 17;
            int closeMin = new Random().nextInt(60);

            openingTimes.put(day, LocalDateTime.of(LocalDate.of(2018, 10, startDay), LocalTime.of(openHour,
                    openMin)));
            closingTimes.put(day, LocalDateTime.of(LocalDate.of(2018, 10, startDay), LocalTime.of(closeHour,
                    closeMin)));

            startDay++;
        }

        store = new Store(utils.randomRecipe(), globalRecipes, new ArrayList<>(), openingTimes, closingTimes, 15.5);


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


}
