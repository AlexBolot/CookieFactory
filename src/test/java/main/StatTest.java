package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Store;
import utils.TestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class StatTest {

    private final TestUtils utils = new TestUtils();
    private Store store;

    @Before
    public void setUp() {
        this.store = new Store("", null,new ArrayList<>(), new ArrayList<>(),new HashMap<>(), new HashMap<>(),0f);
        this.store.setKitchen(utils.getInfiniteMockKitchen());
        this.store.setClosingTime(Day.MONDAY,LocalDateTime.now().plusHours(4));
        this.store.setOpeningTime(Day.MONDAY,LocalDateTime.now().minusHours(4));
    }

    @Test
    public void cookieRatioOnValidOrders() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3));
        Order order2 = new Order(this.store, LocalDateTime.now().plusHours(3));
        order1.setGuest(new Guest(""));
        order2.setGuest(new Guest(""));
        order1.addCookie(utils.recipeFromName("White Dog"),
                1);
        order1.addCookie(utils.recipeFromName("Cookie for nothing"),
                2);
        order1.addCookie(new Recipe("Created recipe",null, null, new ArrayList<>(), null, null,true),
                1);
        order2.addCookie(utils.recipeFromName("Cookie for nothing"),
                1);
        order2.addCookie(new Recipe("My custom recipe",null, null, new ArrayList<>(), null, null,true),
                1);
        order2.addCookie(new Recipe("Recipe 2",null, null, new ArrayList<>(), null, null,true),
                2);
        this.store.placeOrder(order1);
        this.store.placeOrder(order2);
        CookieRatioStat stat = new CookieRatioStat(this.store);
        stat.computeValue();

        Assert.assertEquals(3,stat.getStat().keySet().size());
        Assert.assertEquals((double) 1/(double) 8,stat.getStat().get("White Dog"),0.0);
        Assert.assertEquals((double) 3/(double) 8,stat.getStat().get("Cookie for nothing"),0.0);
        Assert.assertEquals((double) 4/(double) 8,stat.getStat().get("Custom"),0.0);
    }

    @Test
    public void cookieRatioWithOneBigOrderLine() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3));
        order1.setGuest(new Guest(""));
        order1.addCookie(utils.recipeFromName("White Dog"),
                50);
        this.store.placeOrder(order1);
        CookieRatioStat stat = new CookieRatioStat(this.store);
        stat.computeValue();

        Assert.assertEquals(1,stat.getStat().keySet().size());
        Assert.assertEquals(1.0,stat.getStat().get("White Dog"),0.0);
    }


}