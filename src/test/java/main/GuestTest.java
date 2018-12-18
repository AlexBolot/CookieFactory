package main;

import api.BankingData;
import order.Order;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import store.Manager;
import store.Store;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static utils.TestUtils.getFixedClock;
import static utils.TestUtils.getInfiniteMockKitchen;

public class GuestTest {

    private Order order;
    private Guest guest;
    private final ArrayList<Recipe> globalRecipes = new ArrayList<>();
    private final TestUtils utils = new TestUtils();

    private final LocalDateTime testingTime = LocalDateTime.now().withHour(13).withMinute(20);

    @Before
    public void before() {

        CookieFirm.instance().setClock(getFixedClock(testingTime.getHour(), testingTime.getMinute()));

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        Store store = new Store("", utils.randomRecipe(), globalRecipes, new HashMap<>(), new HashMap<>(), 14, 1);

        Manager manager = new Manager(store, "Bob");

        // Each day the store opens at 8h00 before now and closes at 19h00
        for (DayOfWeek day : DayOfWeek.values()) {
            manager.changeOpeningTime(day, LocalTime.of(8, 0));
            manager.changeClosingTime(day, LocalTime.of(19, 0));
        }

        order = new Order(store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order.getStore().setKitchen(getInfiniteMockKitchen());

        order.addCookie(globalRecipes.get(0), 5);

        guest = new Guest();
        guest.setTemporaryOrder(order);
        guest.setBankingData(new BankingData("firstName", "lastName", "058318545"));
    }

    @Test
    public void placeOrder_OnlinePaiment() {
        assertNull(order.getBankingData());
        guest.placeOrder(true);
        assertNotNull(order.getBankingData());
        assertEquals(order.getBankingData(), guest.getBankingData());
    }

    @Test
    public void placeOrder_DeskPaiment() {
        assertNull(order.getBankingData());
        guest.placeOrder(false);
        assertNull(order.getBankingData());
    }

    @Test
    public void guestIDIncrement() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();

        assertEquals(guest1.getId(), guest2.getId() - 1);
    }
}