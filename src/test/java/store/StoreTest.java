package store;

import main.Day;
import main.Guest;
import order.Order;
import order.OrderState;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import recipe.Recipe;
import utils.TestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static utils.TestUtils.getInfiniteMockKitchen;

public class StoreTest {

    private final TestUtils utils = new TestUtils();

    private Store store;
    private Recipe oldRecipe;
    private Recipe newRecipe;

    private final Guest guestBob = new Guest("");
    private final Guest guestAlice = new Guest("");

    private final HashMap<Day, LocalTime> openingTimes = new HashMap<>();
    private final HashMap<Day, LocalTime> closingTimes = new HashMap<>();

    private final ArrayList<Recipe> globalRecipes = new ArrayList<>();

    @Before
    public void before() {

        oldRecipe = utils.randomRecipe();
        newRecipe = utils.randomRecipe();

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        // Each day the store opens 5h before now and closes 5h after now (for easier testing purposes)
        for (Day day : Day.values()) {
            openingTimes.put(day, LocalTime.now().minusHours(5));
            closingTimes.put(day, LocalTime.now().plusHours(5));
        }
        guestAlice.setEmail("Alice");
        guestBob.setEmail("Bob");

        ArrayList<Order> orders = new ArrayList<>();
        store = new Store(oldRecipe, globalRecipes, orders, openingTimes, closingTimes, 15.5);

        store.setKitchen(getInfiniteMockKitchen());

        Order order = new Order(store, LocalDateTime.now());
        order.setGuest(guestBob);
        orders.add(order);


        order = new Order(store, LocalDateTime.now().plusHours(1));
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

        Order emptyOrder = new Order(store, now.plusHours(3));
        Order normalOrder = new Order(store, now.plusHours(3));
        Order tooEarlyOrder = new Order(store, now.minusHours(6));
        Order tooLateOrder = new Order(store, now.plusHours(6));
        Order tooShortOrder = new Order(store, now.plusHours(1));

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

    @Ignore
    @Test
    public void placeOrder() {

        LocalDateTime now = LocalDateTime.now();

        Order normalOrder = new Order(store, now.plusHours(3));

        for (int i = 1; i < 4; i++) {
            normalOrder.addCookie(utils.randomRecipe(), i);
        }

        assertTrue(0 <store.placeOrder(normalOrder));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placeOrderEmpty(){
        LocalDateTime now = LocalDateTime.now();

        Order emptyOrder = new Order(store, now.plusHours(3));

        store.placeOrder(emptyOrder);
    }

    @Test
    public void payingAnOrder(){
        LocalDateTime now = LocalDateTime.now();

        Order normalOrder = new Order(store, now.plusHours(3));
        normalOrder.addCookie(utils.randomRecipe(), 10);
        normalOrder.setGuest(guestAlice);
        store.placeOrder(normalOrder);
        assertFalse(normalOrder.isPayed());
        store.setStatusPaymentOrder(Day.TUESDAY, now.plusHours(3), "Alice");
        assertTrue(normalOrder.isPayed());
    }

    @Test
    public void findOrderFromDayTimeAndEmail() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        Guest guest = new Guest("");
        guest.setEmail("email");
        final Order order = new Order(store, pickUpTime);
        order.setGuest(guest);
        store.getOrders().add(order);
        assertEquals(store.findOrder(pickUpTime, guest.getEmail()).orElse(null), order);
    }

    @Test
    public void findOrderWithDuplicateCriterias() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        final Order order = new Order(store, pickUpTime);
        store.getOrders().add(order);
        order.setGuest(guestAlice);
        assertEquals(store.findOrder(pickUpTime, guestAlice.getEmail()).orElse(null), order);
    }

    @Test
    public void returnsEmptyOptionalOnEmptyOrderList() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        store = new Store(utils.randomRecipe(), new ArrayList<>(),new ArrayList<>(), new HashMap<>(),new HashMap<>(),1.0);
        assertFalse(store.findOrder(pickUpTime, guestAlice.getEmail()).isPresent());
    }

    @Test
    public void emptyWhenOrderNotFound() {
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        assertFalse(store.findOrder(pickUpTime, guestAlice.getEmail()).isPresent());
    }

    @Test
    public void cancelOrder(){
        Day pickUpDay = Day.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now().plusHours(3);
        final Order order = new Order(store, pickUpTime);
        order.addCookie(utils.randomRecipe(), 2);
        Guest guest = new Guest("");
        guest.setTemporaryOrder(order);

        assertEquals(OrderState.DRAFT, order.getState());

        guest.placeOrder(true);

        assertEquals(OrderState.ORDERED, order.getState());

        store.cancelOrder(order);

        assertEquals(OrderState.CANCELED, order.getState());

    }
}