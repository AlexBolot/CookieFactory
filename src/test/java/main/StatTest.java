package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Store;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StatTest {

    private final TestUtils utils = new TestUtils();
    private final Catalog catalog = new Catalog();
    private Store store;

    @Before
    public void setUp() {
        this.store = new Store("", null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 0f);
        this.store.setKitchen(TestUtils.getInfiniteMockKitchen());
        this.store.setClosingTime(DayOfWeek.MONDAY, LocalTime.now().plusHours(4));
        this.store.setOpeningTime(DayOfWeek.MONDAY, LocalTime.now().minusHours(4));
    }

    @Test
    public void cookieRatioOnValidOrders() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        Order order2 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest());
        order2.setGuest(new Guest());
        order1.addCookie(utils.recipeFromName("White Dog"),
                1);
        order1.addCookie(utils.recipeFromName("Cookie for nothing"),
                2);
        order1.addCookie(new Recipe("Created recipe", null, null, new ArrayList<>(), null, null, true),
                1);
        order2.addCookie(utils.recipeFromName("Cookie for nothing"),
                1);
        order2.addCookie(new Recipe("My custom recipe", null, null, new ArrayList<>(), null, null, true),
                1);
        order2.addCookie(new Recipe("Recipe 2", null, null, new ArrayList<>(), null, null, true),
                2);
        this.store.placeOrder(order1);
        this.store.placeOrder(order2);
        CookieRatioStat stat = new CookieRatioStat(this.store);
        stat.computeValue();

        Assert.assertEquals(3, stat.getStat().keySet().size());
        Assert.assertEquals((double) 1 / (double) 8, stat.getStat().get("White Dog"), 0.0);
        Assert.assertEquals((double) 3 / (double) 8, stat.getStat().get("Cookie for nothing"), 0.0);
        Assert.assertEquals((double) 4 / (double) 8, stat.getStat().get("Custom"), 0.0);
    }

    @Test
    public void cookieRatioWithOneBigOrderLine() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(utils.recipeFromName("White Dog"),
                50);
        this.store.placeOrder(order1);
        CookieRatioStat stat = new CookieRatioStat(this.store);
        stat.computeValue();

        Assert.assertEquals(1, stat.getStat().keySet().size());
        Assert.assertEquals(1.0, stat.getStat().get("White Dog"), 0.0);
    }

    @Test
    public void weightedIngredientStatTest1Cookie() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest(""));
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(0),
                        catalog.getFlavorList().get(0),
                        Arrays.asList(catalog.getToppingList().get(0),
                                catalog.getToppingList().get(1)),
                        catalog.getMixList().get(0),
                        catalog.getCookingList().get(0),
                        true),
                1);
        this.store.placeOrder(order1);
        WeightedIngredientCustomStat stat = new WeightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1,value.getDoughRatio().size());
        Assert.assertEquals(1,value.getFlavorRatio().size());
        Assert.assertEquals(2,value.getToppingRatio().size());
        Assert.assertEquals(1,value.getMixRatio().size());
        Assert.assertEquals(1,value.getCookingRatio().size());
        Assert.assertEquals(1.0,value.getDoughRatio().get(catalog.getDoughList().get(0)),0.0);
        Assert.assertEquals(1.0,value.getFlavorRatio().get(catalog.getFlavorList().get(0)),0.0);
        Assert.assertEquals(0.5,value.getToppingRatio().get(catalog.getToppingList().get(0)),0.0);
        Assert.assertEquals(0.5,value.getToppingRatio().get(catalog.getToppingList().get(1)),0.0);
        Assert.assertEquals(1.0,value.getMixRatio().get(catalog.getMixList().get(0)),0.0);
        Assert.assertEquals(1.0,value.getCookingRatio().get(catalog.getCookingList().get(0)),0.0);
    }
    @Test
    public void weightedStatWithNonCustom() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest(""));
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(1),
                        catalog.getFlavorList().get(1),
                        Arrays.asList(catalog.getToppingList().get(2),
                                catalog.getToppingList().get(3)),
                        catalog.getMixList().get(1),
                        catalog.getCookingList().get(1),
                        true),
                1);
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(0),
                        catalog.getFlavorList().get(0),
                        Arrays.asList(catalog.getToppingList().get(0),
                                catalog.getToppingList().get(1)),
                        catalog.getMixList().get(0),
                        catalog.getCookingList().get(0),
                        false),
                1);
        this.store.placeOrder(order1);
        WeightedIngredientCustomStat stat = new WeightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1,value.getDoughRatio().size());
        Assert.assertEquals(1,value.getFlavorRatio().size());
        Assert.assertEquals(2,value.getToppingRatio().size());
        Assert.assertEquals(1,value.getMixRatio().size());
        Assert.assertEquals(1,value.getCookingRatio().size());
        Assert.assertEquals(1.0,value.getDoughRatio().get(catalog.getDoughList().get(1)),0.0);
        Assert.assertEquals(1.0,value.getFlavorRatio().get(catalog.getFlavorList().get(1)),0.0);
        Assert.assertEquals(0.5,value.getToppingRatio().get(catalog.getToppingList().get(2)),0.0);
        Assert.assertEquals(0.5,value.getToppingRatio().get(catalog.getToppingList().get(3)),0.0);
        Assert.assertEquals(1.0,value.getMixRatio().get(catalog.getMixList().get(1)),0.0);
        Assert.assertEquals(1.0,value.getCookingRatio().get(catalog.getCookingList().get(1)),0.0);
    }
    @Test
    public void unweightedStatWithMultipleCookies() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest(""));
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(0),
                        catalog.getFlavorList().get(1),
                        Arrays.asList(catalog.getToppingList().get(2),
                                catalog.getToppingList().get(3)),
                        catalog.getMixList().get(1),
                        catalog.getCookingList().get(1),
                        true),
                1);
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(0),
                        catalog.getFlavorList().get(0),
                        Arrays.asList(catalog.getToppingList().get(0),
                                catalog.getToppingList().get(1)),
                        catalog.getMixList().get(1),
                        catalog.getCookingList().get(0),
                        true),
                1);
        this.store.placeOrder(order1);
        UnweightedIngredientCustomStat stat = new UnweightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1,value.getDoughRatio().size());
        Assert.assertEquals(2,value.getFlavorRatio().size());
        Assert.assertEquals(4,value.getToppingRatio().size());
        Assert.assertEquals(1,value.getMixRatio().size());
        Assert.assertEquals(2,value.getCookingRatio().size());
        Assert.assertEquals(1.0,value.getDoughRatio().get(catalog.getDoughList().get(0)),0.0);
        Assert.assertEquals(0.5,value.getFlavorRatio().get(catalog.getFlavorList().get(0)),0.0);
        Assert.assertEquals(0.5,value.getFlavorRatio().get(catalog.getFlavorList().get(1)),0.0);
        Assert.assertEquals(0.25,value.getToppingRatio().get(catalog.getToppingList().get(0)),0.0);
        Assert.assertEquals(0.25,value.getToppingRatio().get(catalog.getToppingList().get(1)),0.0);
        Assert.assertEquals(0.25,value.getToppingRatio().get(catalog.getToppingList().get(2)),0.0);
        Assert.assertEquals(0.25,value.getToppingRatio().get(catalog.getToppingList().get(3)),0.0);
        Assert.assertEquals(1.0,value.getMixRatio().get(catalog.getMixList().get(1)),0.0);
        Assert.assertEquals(0.5,value.getCookingRatio().get(catalog.getCookingList().get(0)),0.0);
        Assert.assertEquals(0.5,value.getCookingRatio().get(catalog.getCookingList().get(1)),0.0);
    }

    @Test
    public void weightedStatWithMultipleCookies() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest(""));
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(0),
                        catalog.getFlavorList().get(1),
                        Arrays.asList(catalog.getToppingList().get(2),
                                catalog.getToppingList().get(3)),
                        catalog.getMixList().get(1),
                        catalog.getCookingList().get(1),
                        true),
                1);
        order1.addCookie(new Recipe("Created recipe",
                        catalog.getDoughList().get(0),
                        catalog.getFlavorList().get(0),
                        Arrays.asList(catalog.getToppingList().get(0),
                                catalog.getToppingList().get(1)),
                        catalog.getMixList().get(1),
                        catalog.getCookingList().get(0),
                        true),
                3);
        this.store.placeOrder(order1);
        WeightedIngredientCustomStat stat = new WeightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1,value.getDoughRatio().size());
        Assert.assertEquals(2,value.getFlavorRatio().size());
        Assert.assertEquals(4,value.getToppingRatio().size());
        Assert.assertEquals(1,value.getMixRatio().size());
        Assert.assertEquals(2,value.getCookingRatio().size());
        Assert.assertEquals(1.0,value.getDoughRatio().get(catalog.getDoughList().get(0)),0.0);
        Assert.assertEquals(0.75,value.getFlavorRatio().get(catalog.getFlavorList().get(0)),0.0);
        Assert.assertEquals(0.25,value.getFlavorRatio().get(catalog.getFlavorList().get(1)),0.0);
        Assert.assertEquals(0.375,value.getToppingRatio().get(catalog.getToppingList().get(0)),0.0);
        Assert.assertEquals(0.375,value.getToppingRatio().get(catalog.getToppingList().get(1)),0.0);
        Assert.assertEquals(0.125,value.getToppingRatio().get(catalog.getToppingList().get(2)),0.0);
        Assert.assertEquals(0.125,value.getToppingRatio().get(catalog.getToppingList().get(3)),0.0);
        Assert.assertEquals(1.0,value.getMixRatio().get(catalog.getMixList().get(1)),0.0);
        Assert.assertEquals(0.75,value.getCookingRatio().get(catalog.getCookingList().get(0)),0.0);
        Assert.assertEquals(0.25,value.getCookingRatio().get(catalog.getCookingList().get(1)),0.0);
    }
}