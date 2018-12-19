package store;

import api.BankingData;
import main.CookieFirm;
import main.Guest;
import order.Order;
import order.OrderState;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static utils.TestUtils.getFixedClock;
import static utils.TestUtils.getInfiniteMockKitchen;

public class StoreTest {

    private final TestUtils utils = new TestUtils();

    private Store store;
    private Recipe oldRecipe;
    private Recipe newRecipe;

    private final Guest guestBob = new Guest();
    private final Guest guestAlice = new Guest();

    private final HashMap<DayOfWeek, LocalTime> openingTimes = new HashMap<>();
    private final HashMap<DayOfWeek, LocalTime> closingTimes = new HashMap<>();

    private final ArrayList<Recipe> globalRecipes = new ArrayList<>();
    private final LocalDateTime testingTime = LocalDateTime.now().withHour(13).withMinute(20);

    @Before
    public void setUp() {

        CookieFirm.instance().setClock(getFixedClock(testingTime.getHour(), testingTime.getMinute()));

        oldRecipe = utils.randomRecipe();
        newRecipe = utils.randomRecipe();

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        // Each day the store opens at 8h00 before now and closes at 19h00
        for (DayOfWeek day : DayOfWeek.values()) {
            openingTimes.put(day, LocalTime.of(8, 0));
            closingTimes.put(day, LocalTime.of(19, 0));
        }

        guestAlice.setEmail("Alice");
        guestBob.setEmail("Bob");

        ArrayList<Order> orders = new ArrayList<>();
        store = new Store("", oldRecipe, globalRecipes, openingTimes, closingTimes, 15.5, 1);

        store.setKitchen(getInfiniteMockKitchen());

        Order order = new Order(store, testingTime);
        order.setGuest(guestBob);
        orders.add(order);


        order = new Order(store, testingTime.plusHours(1));
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
        Order normalOrder = new Order(store, testingTime.plusHours(3));
        IntStream.range(1, 4).forEach(i -> normalOrder.addCookie(utils.randomRecipe(), i));
        assertTrue(store.checkOrderValidity(normalOrder));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkOrderValidityEmpty() {
        Order emptyOrder = new Order(store, testingTime.plusHours(3));
        store.checkOrderValidity(emptyOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkOrderValidityTooEarly() {

        //this is tomorrow but 1h before the store's opening
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(7, 0));

        Order tooEarlyOrder = new Order(store, dateTime);
        IntStream.range(1, 4).forEach(i -> tooEarlyOrder.addCookie(utils.randomRecipe(), i));
        store.checkOrderValidity(tooEarlyOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkOrderValidityTooLate() {

        //this 1h after the store's closing
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0));

        Order tooLateOrder = new Order(store, dateTime);
        IntStream.range(1, 4).forEach(i -> tooLateOrder.addCookie(utils.randomRecipe(), i));
        store.checkOrderValidity(tooLateOrder);
    }

    @Test
    public void placeOrder() {
        Order normalOrder = new Order(store, testingTime.plusHours(3));
        normalOrder.setGuest(new Guest());
        for (int i = 1; i < 4; i++) {
            normalOrder.addCookie(utils.randomRecipe(), i);
        }

        assertTrue(0 < store.placeOrder(normalOrder));
    }

    @Test(expected = IllegalArgumentException.class)
    public void placeOrderEmpty() {
        Order emptyOrder = new Order(store, testingTime.plusHours(3));

        store.placeOrder(emptyOrder);
    }

    @Ignore
    @Test
    public void payingAnOrder() {
        Order normalOrder = new Order(store, testingTime.plusHours(3));
        normalOrder.addCookie(utils.randomRecipe(), 10);
        normalOrder.setGuest(guestAlice);
        store.placeOrder(normalOrder);
        //assertFalse(normalOrder.isPayed());
        store.setStatusPaymentOrder(DayOfWeek.TUESDAY, testingTime.plusHours(3), "Alice");
        //assertTrue(normalOrder.isPayed());
    }

    @Test
    public void findOrderFromDayTimeAndEmail() {
        LocalDateTime pickUpTime = LocalDateTime.now();
        Guest guest = new Guest();
        guest.setEmail("email");
        final Order order = new Order(store, pickUpTime);
        order.setGuest(guest);
        store.getOrders().add(order);
        assertEquals(store.findOrder(pickUpTime, guest.getEmail()).orElse(null), order);
    }

    @Test
    public void findOrderWithDuplicateCriterias() {
        LocalDateTime pickUpTime = testingTime;
        final Order order = new Order(store, pickUpTime);
        store.getOrders().add(order);
        order.setGuest(guestAlice);
        assertEquals(store.findOrder(pickUpTime, guestAlice.getEmail()).orElse(null), order);
    }

    @Test
    public void returnsEmptyOptionalOnEmptyOrderList() {
        store = new Store("", utils.randomRecipe(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1.0, 1);
        assertFalse(store.findOrder(testingTime, guestAlice.getEmail()).isPresent());
    }

    @Test
    public void emptyWhenOrderNotFound() {
        assertFalse(store.findOrder(testingTime, guestAlice.getEmail()).isPresent());
    }


    @Test
    public void cancelOrder() {
        LocalDateTime pickUpTime = testingTime.plusHours(3);
        Guest guest = new Guest();
        guest.setBankingData(new BankingData("John","Doe","A3535NG"));
        Order order = guest.getTemporaryOrder();
        order.setPickUpTime(pickUpTime);
        order.setStore(store);
        order.addCookie(utils.randomRecipe(), 2);

        assertEquals(OrderState.DRAFT, order.getState());

        guest.placeOrder(true);

        assertEquals(OrderState.ORDERED, order.getState());

        store.cancelOrder(order);

        assertEquals(OrderState.CANCELED, order.getState());
    }

    @Test
    public void computeRecipeeBasedOnIngredients() {
        Catalog catalog = new Catalog();
        Recipe recipe = new Recipe("plain",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                catalog.getToppingList().subList(0, 1),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                false);
        Kitchen kitchen = store.getKitchen();
        when(kitchen.vendingPriceOf(any())).thenReturn(1.0);
        assertEquals(3.0, store.getRecipePrice(recipe), 0.0001);
        when(kitchen.vendingPriceOf(any())).thenReturn(3.0);
        assertEquals(9.0, store.getRecipePrice(recipe), 0.0001);
    }

    @Test
    public void applyCurstomRecipeePrice() {
        Catalog catalog = new Catalog();
        Recipe recipe = new Recipe("plain",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                catalog.getToppingList().subList(0, 1),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                true);
        Kitchen kitchen = store.getKitchen();
        store.setCustomRecipeeMargin(1.0);
        when(kitchen.vendingPriceOf(any())).thenReturn(1.0);
        assertEquals(4.0, store.getRecipePrice(recipe), 0.0001);
        when(kitchen.vendingPriceOf(any())).thenReturn(3.0);
        assertEquals(10.0, store.getRecipePrice(recipe), 0.0001);
        store.setCustomRecipeeMargin(21.12);
        assertEquals(30.12, store.getRecipePrice(recipe), 0.0001);
    }
}