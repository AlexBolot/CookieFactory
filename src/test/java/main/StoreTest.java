package main;

import order.Order;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class StoreTest {

    private TestUtils utils = new TestUtils();

    private Store store;
    private Recipe oldRecipe;
    private Recipe newRecipe;

    private Guest guestBob = new Guest("");
    private Guest guestAlice = new Guest("");

    private HashMap<Day, LocalDateTime> openingTimes = new HashMap<>();
    private HashMap<Day, LocalDateTime> closingTimes = new HashMap<>();

    private ArrayList<Recipe> globalRecipes = new ArrayList<>();

    @Before
    public void before() {

        oldRecipe = utils.randomRecipe();
        newRecipe = utils.randomRecipe();

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        // Each day the store opens 5h before now and closes 5h after now (for easier testing purposes)
        for (Day day : Day.values()) {
            openingTimes.put(day, LocalDateTime.now().minusHours(5));
            closingTimes.put(day, LocalDateTime.now().plusHours(5));
        }
        guestAlice.setEmail("Alice");
        guestBob.setEmail("Bob");

        ArrayList<Order> orders = new ArrayList<>();
        store = new Store(oldRecipe, globalRecipes, orders, openingTimes, closingTimes, 15.5);

        Order order = new Order(store, LocalDateTime.now(), Day.TUESDAY);
        order.setGuest(guestBob);
        orders.add(order);

        order = new Order(store, LocalDateTime.now().plusHours(1), Day.FRIDAY);
        order.addCookie(globalRecipes.get(0), 1);
        order.addCookie(globalRecipes.get(2), 3);
        order.setGuest(guestAlice);

        orders.add(order);
    }

    @Test
    public void getRecipes() {
        ArrayList<Recipe> expected = new ArrayList<>(globalRecipes);
        expected.add(oldRecipe);

        assertEquals(expected, store.getRecipes());
    }

    @Test
    public void openingTime() {
        for (Day day : Day.values()) {
            assertEquals(openingTimes.get(day), store.openingTime(day));
        }
    }

    @Test
    public void closingTime() {
        for (Day day : Day.values()) {
            assertEquals(closingTimes.get(day), store.closingTime(day));
        }
    }

    @Test
    public void setMonthlyRecipeValid() {
        store.setMonthlyRecipe(newRecipe);

        assertEquals(newRecipe, store.getMonthlyRecipe());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMonthlyRecipeInvalid() {
        store.setMonthlyRecipe(oldRecipe);
    }

    @Test
    public void checkOrderValidity() {

        LocalDateTime now = LocalDateTime.now();

        Order emptyOrder = new Order(store, now.plusHours(3), Day.TUESDAY);
        Order normalOrder = new Order(store, now.plusHours(3), Day.TUESDAY);
        Order tooEarlyOrder = new Order(store, now.minusHours(6), Day.TUESDAY);
        Order tooLateOrder = new Order(store, now.plusHours(6), Day.TUESDAY);
        Order tooShortOrder = new Order(store, now.plusHours(1), Day.TUESDAY);

        for (int i = 1; i < 4; i++) {
            normalOrder.addCookie(utils.randomRecipe(), i);
            tooEarlyOrder.addCookie(utils.randomRecipe(), i);
            tooLateOrder.addCookie(utils.randomRecipe(), i);
        }

        assertTrue(store.checkOrderValidity(normalOrder));
        assertFalse(store.checkOrderValidity(emptyOrder));
        assertFalse(store.checkOrderValidity(tooEarlyOrder));
        assertFalse(store.checkOrderValidity(tooLateOrder));
        assertFalse(store.checkOrderValidity(tooShortOrder));
    }

    @Test
    public void placeOrder() {

        LocalDateTime now = LocalDateTime.now();

        Order normalOrder = new Order(store, now.plusHours(3), Day.TUESDAY);
        Order emptyOrder = new Order(store, now.plusHours(3), Day.TUESDAY);

        for (int i = 1; i < 4; i++) {
            normalOrder.addCookie(utils.randomRecipe(), i);
        }

        assertTrue(store.placeOrder(normalOrder));
        assertFalse(store.placeOrder(emptyOrder));
    }


    @Test
    public void payingAnOrder(){
        LocalDateTime now = LocalDateTime.now();

        Order normalOrder = new Order(store, now.plusHours(3), Day.TUESDAY);
        normalOrder.addCookie(utils.randomRecipe(), 10);
        store.placeOrder(normalOrder);
        assertFalse(normalOrder.isPayed());
        store.setStatusPaymentOrder(Day.TUESDAY, now.plusHours(3), new Guest(""));
        assertTrue(normalOrder.isPayed());
    }

    @Test
    public void findOrderFromDayTimeAndEmail() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        Guest guest = new Guest("");
        guest.setEmail("email");
        final Order order = new Order(store, pickUpTime, pickUpDay);
        order.setGuest(guest);
        store.getOrders().add(order);
        assertEquals(store.findOrder(pickUpTime, pickUpDay, guest.getEmail()).orElse(null), order);
    }

    @Test
    public void findOrderWithDuplicateCriterias() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        final Order order = new Order(store, pickUpTime, pickUpDay);
        store.getOrders().add(order);
        order.setGuest(guestAlice);
        assertEquals(store.findOrder(pickUpTime, pickUpDay, guestAlice.getEmail()).orElse(null), order);
    }

    @Test
    public void emptyWhenOrderNotFound() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        assertFalse(store.findOrder(pickUpTime, pickUpDay, guestAlice.getEmail()).isPresent());
    }

}