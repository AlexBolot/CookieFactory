package store;

import main.Guest;
import order.Order;
import order.OrderState;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import recipe.Recipe;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static utils.TestUtils.getInfiniteMockKitchen;

public class StoreTest {

    private final TestUtils utils = new TestUtils();

    private Store store;
    private Recipe oldRecipe;
    private Recipe newRecipe;

    private final Guest guestBob = new Guest("");
    private final Guest guestAlice = new Guest("");

    private final HashMap<DayOfWeek, LocalTime> openingTimes = new HashMap<>();
    private final HashMap<DayOfWeek, LocalTime> closingTimes = new HashMap<>();

    private final ArrayList<Recipe> globalRecipes = new ArrayList<>();
    private final LocalDateTime now = LocalDateTime.now();

    @Before
    public void before() {

        oldRecipe = utils.randomRecipe();
        newRecipe = utils.randomRecipe();

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        // Each day the store opens 5h before now and closes 5h after now (for easier testing purposes)
        for (DayOfWeek day : DayOfWeek.values()) {
            openingTimes.put(day, LocalTime.now().minusHours(5));
            closingTimes.put(day, LocalTime.now().plusHours(5));
        }
        guestAlice.setEmail("Alice");
        guestBob.setEmail("Bob");

        ArrayList<Order> orders = new ArrayList<>();
        store = new Store("", oldRecipe, globalRecipes, orders, openingTimes, closingTimes, 15.5);

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
        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(openingTimes.get(day), store.openingTime(day));
        }
    }

    @Test
    public void closingTime() {
        for (DayOfWeek day : DayOfWeek.values()) {
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
        Order normalOrder = new Order(store, now.plusHours(3));
        IntStream.range(1, 4).forEach(i -> normalOrder.addCookie(utils.randomRecipe(), i));
        assertTrue(store.checkOrderValidity(normalOrder));
    }

    @Test (expected = IllegalArgumentException.class)
    public void checkOrderValidityEmpty(){
        Order emptyOrder = new Order(store, now.plusHours(3));
        store.checkOrderValidity(emptyOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkOrderValidityTooEarly(){
        Order tooEarlyOrder = new Order(store, now.minusHours(6));
        IntStream.range(1, 4).forEach(i -> tooEarlyOrder.addCookie(utils.randomRecipe(), i));
        store.checkOrderValidity(tooEarlyOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkOrderValidityTooLate(){
        Order tooLateOrder = new Order(store, now.plusHours(6));
        IntStream.range(1, 4).forEach(i -> tooLateOrder.addCookie(utils.randomRecipe(), i));
        store.checkOrderValidity(tooLateOrder);
    }

    @Ignore
    @Test
    public void placeOrder() {
        Order normalOrder = new Order(store, now.plusHours(3));

        for (int i = 1; i < 4; i++) {
            normalOrder.addCookie(utils.randomRecipe(), i);
        }

        assertTrue(0 <store.placeOrder(normalOrder));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placeOrderEmpty(){
        Order emptyOrder = new Order(store, now.plusHours(3));

        store.placeOrder(emptyOrder);
    }

    @Test
    public void payingAnOrder(){
        Order normalOrder = new Order(store, now.plusHours(3));
        normalOrder.addCookie(utils.randomRecipe(), 10);
        normalOrder.setGuest(guestAlice);
        store.placeOrder(normalOrder);
        assertFalse(normalOrder.isPayed());
        store.setStatusPaymentOrder(DayOfWeek.TUESDAY, now.plusHours(3), "Alice");
        assertTrue(normalOrder.isPayed());
    }

    @Test
    public void findOrderFromDayTimeAndEmail() {
        DayOfWeek pickUpDay = DayOfWeek.TUESDAY;
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
        DayOfWeek pickUpDay = DayOfWeek.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        final Order order = new Order(store, pickUpTime);
        store.getOrders().add(order);
        order.setGuest(guestAlice);
        assertEquals(store.findOrder(pickUpTime, guestAlice.getEmail()).orElse(null), order);
    }

    @Test
    public void returnsEmptyOptionalOnEmptyOrderList() {
        DayOfWeek pickUpDay = DayOfWeek.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        store = new Store("",utils.randomRecipe(), new ArrayList<>(),new ArrayList<>(), new HashMap<>(),new HashMap<>
                (),1.0);
        assertFalse(store.findOrder(pickUpTime, guestAlice.getEmail()).isPresent());
    }

    @Test
    public void emptyWhenOrderNotFound() {
        DayOfWeek pickUpDay = DayOfWeek.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now();
        assertFalse(store.findOrder(pickUpTime, guestAlice.getEmail()).isPresent());
    }

    @Test
    public void cancelOrder(){
        DayOfWeek pickUpDay = DayOfWeek.TUESDAY;
        LocalDateTime pickUpTime = LocalDateTime.now().plusHours(3);
        Guest guest = new Guest("");
        Order order  = guest.getTemporaryOrder();
        order.setPickUpTime(pickUpTime);
        order.setStore(store);
        order.addCookie(utils.randomRecipe(), 2);

        assertEquals(OrderState.DRAFT, order.getState());

        guest.placeOrder(true);

        assertEquals(OrderState.ORDERED, order.getState());

        store.cancelOrder(order);

        assertEquals(OrderState.CANCELED, order.getState());

    }
}