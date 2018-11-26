package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import store.Store;
import utils.TestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static java.time.DayOfWeek.MONDAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static utils.TestUtils.getInfiniteMockKitchen;

public class GuestTest {

    private Order order;
    private Guest guest;
    private final ArrayList<Recipe> globalRecipes = new ArrayList<>();
    private final TestUtils utils = new TestUtils();

    @Before
    public void before() {

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        Store store = new Store(utils.randomRecipe(), globalRecipes, new ArrayList<>(), new HashMap<>(), new HashMap<>(),14);

        store.setOpeningTime(MONDAY, LocalTime.now().minusHours(6));
        store.setClosingTime(MONDAY, LocalTime.now().plusHours(6));

        order = new Order(store, LocalDateTime.now().plusHours(3));
        order.getStore().setKitchen(getInfiniteMockKitchen());

        order.addCookie(globalRecipes.get(0), 5);

        guest = new Guest("");
        guest.setTemporaryOrder(order);
    }

    @Test
    public void placeOrder_OnlinePaiment() {
        assertFalse(order.isPayed());
        guest.placeOrder(true);
        Assert.assertTrue(order.isPayed());
    }

    @Test
    public void placeOrder_DeskPaiment() {
        assertFalse(order.isPayed());
        guest.placeOrder(false);
        assertFalse(order.isPayed());
        assertEquals(guest, order.getGuest());
    }

    @Test (expected = IllegalStateException.class)
    public void placeOrder_AlreadyPaid() {
        order.setPayed();
        guest.placeOrder(true);
    }
}