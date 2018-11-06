package main;

import order.Order;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StoreTest {

    private TestUtils utils = new TestUtils();

    private Store store;
    private Recipe oldRecipe;
    private Recipe newRecipe;

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

        int startDay = 5;

        for (Day day : Day.values()) {

            int openHour = new Random().nextInt(2) + 7;
            int openMin = new Random().nextInt(60);
            int closeHour = new Random().nextInt(2) + 17;
            int closeMin = new Random().nextInt(60);

            openingTimes.put(day, LocalDateTime.of(LocalDate.of(2018, 11, startDay), LocalTime.of(openHour, openMin)));
            closingTimes.put(day, LocalDateTime.of(LocalDate.of(2018, 11, startDay), LocalTime.of(closeHour, closeMin)));

            startDay++;
        }

        store = new Store(oldRecipe, new Manager(), globalRecipes, new ArrayList<>(), openingTimes, closingTimes, 15.5);
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

        Order emptyOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(now.plusHours(3))), Day.TUESDAY);
        Order normalOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(now.plusHours(3))), Day.TUESDAY);
        Order tooEarlyOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 30)), Day.TUESDAY);
        Order tooLateOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 30)), Day.TUESDAY);
        Order tooShortOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(now.plusHours(1))), Day.TUESDAY);

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
    public void placeOrder(){

        LocalDateTime now = LocalDateTime.now();

        Order normalOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(now.plusHours(3))), Day.TUESDAY);
        Order faultyOrder = new Order(store, LocalDateTime.of(LocalDate.now(), LocalTime.from(now.plusHours(3))), Day.TUESDAY);

        for (int i = 1; i < 4; i++) {
            normalOrder.addCookie(utils.randomRecipe(), i);
        }

        assertTrue(store.placeOrder(normalOrder));
        assertFalse(store.placeOrder(faultyOrder));
    }
}