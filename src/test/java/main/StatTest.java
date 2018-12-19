package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import statistics.*;
import store.Manager;
import store.Store;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.getFixedClock;

public class StatTest {

    private final TestUtils utils = new TestUtils();
    private final Catalog catalog = new Catalog();
    private Store store;

    private final LocalDateTime testingTime = LocalDateTime.now().withHour(13).withMinute(20);

    @Before
    public void setUp() {

        CookieFirm.instance().setClock(getFixedClock(testingTime.getHour(), testingTime.getMinute()));

        this.store = new Store("", null, new ArrayList<>(), new HashMap<>(), new HashMap<>(), 0f, 1);
        this.store.setKitchen(TestUtils.getInfiniteMockKitchen());
        Manager manager = new Manager(store, "Bob");

        // Each day the store opens at 8h00 before now and closes at 19h00
        for (DayOfWeek day : DayOfWeek.values()) {
            manager.changeOpeningTime(day, LocalTime.of(8, 0));
            manager.changeClosingTime(day, LocalTime.of(19, 0));
        }
    }

    @Test
    public void cookieRatioOnValidOrders() {
        LocalDateTime pickupTime = testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        Order order1 = new Order(this.store, pickupTime);
        Order order2 = new Order(this.store, pickupTime);
        order1.setGuest(new Guest());
        order2.setGuest(new Guest());
        order1.addCookie(utils.recipeFromName("White Dog"), 1);
        order1.addCookie(utils.recipeFromName("Cookie for nothing"), 2);
        order1.addCookie(new Recipe("Created recipe", null, null, new ArrayList<>(), null, null, true), 1);
        order2.addCookie(utils.recipeFromName("Cookie for nothing"), 1);
        order2.addCookie(new Recipe("My custom recipe", null, null, new ArrayList<>(), null, null, true), 1);
        order2.addCookie(new Recipe("Recipe 2", null, null, new ArrayList<>(), null, null, true), 2);
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
        Order order1 = new Order(this.store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(utils.recipeFromName("White Dog"), 50);
        this.store.placeOrder(order1);
        CookieRatioStat stat = new CookieRatioStat(this.store);
        stat.computeValue();

        Assert.assertEquals(1, stat.getStat().keySet().size());
        Assert.assertEquals(1.0, stat.getStat().get("White Dog"), 0.0);
    }

    @Test
    public void weightedIngredientStatTest1Cookie() {
        Order order1 = new Order(this.store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0), catalog.getToppingList().get(1)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                true), 1);
        this.store.placeOrder(order1);
        WeightedIngredientCustomStat stat = new WeightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1, value.getDoughRatio().size());
        Assert.assertEquals(1, value.getFlavorRatio().size());
        Assert.assertEquals(2, value.getToppingRatio().size());
        Assert.assertEquals(1, value.getMixRatio().size());
        Assert.assertEquals(1, value.getCookingRatio().size());
        Assert.assertEquals(1.0, value.getDoughRatio().get(catalog.getDoughList().get(0)), 0.0);
        Assert.assertEquals(1.0, value.getFlavorRatio().get(catalog.getFlavorList().get(0)), 0.0);
        Assert.assertEquals(0.5, value.getToppingRatio().get(catalog.getToppingList().get(0)), 0.0);
        Assert.assertEquals(0.5, value.getToppingRatio().get(catalog.getToppingList().get(1)), 0.0);
        Assert.assertEquals(1.0, value.getMixRatio().get(catalog.getMixList().get(0)), 0.0);
        Assert.assertEquals(1.0, value.getCookingRatio().get(catalog.getCookingList().get(0)), 0.0);
    }

    @Test
    public void weightedStatWithNonCustom() {
        Order order1 = new Order(this.store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(1),
                catalog.getFlavorList().get(1),
                Arrays.asList(catalog.getToppingList().get(2), catalog.getToppingList().get(3)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                true), 1);
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0), catalog.getToppingList().get(1)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                false), 1);
        this.store.placeOrder(order1);
        Statistic<IngredientRatio> stat = new WeightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1, value.getDoughRatio().size());
        Assert.assertEquals(1, value.getFlavorRatio().size());
        Assert.assertEquals(2, value.getToppingRatio().size());
        Assert.assertEquals(1, value.getMixRatio().size());
        Assert.assertEquals(1, value.getCookingRatio().size());
        Assert.assertEquals(1.0, value.getDoughRatio().get(catalog.getDoughList().get(1)), 0.0);
        Assert.assertEquals(1.0, value.getFlavorRatio().get(catalog.getFlavorList().get(1)), 0.0);
        Assert.assertEquals(0.5, value.getToppingRatio().get(catalog.getToppingList().get(2)), 0.0);
        Assert.assertEquals(0.5, value.getToppingRatio().get(catalog.getToppingList().get(3)), 0.0);
        Assert.assertEquals(1.0, value.getMixRatio().get(catalog.getMixList().get(1)), 0.0);
        Assert.assertEquals(1.0, value.getCookingRatio().get(catalog.getCookingList().get(1)), 0.0);
    }

    @Test
    public void unweightedStatWithMultipleCookies() {
        Order order1 = new Order(this.store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(1),
                Arrays.asList(catalog.getToppingList().get(2), catalog.getToppingList().get(3)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                true), 1);
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0), catalog.getToppingList().get(1)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(0),
                true), 1);
        this.store.placeOrder(order1);
        UnweightedIngredientCustomStat stat = new UnweightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1, value.getDoughRatio().size());
        Assert.assertEquals(2, value.getFlavorRatio().size());
        Assert.assertEquals(4, value.getToppingRatio().size());
        Assert.assertEquals(1, value.getMixRatio().size());
        Assert.assertEquals(2, value.getCookingRatio().size());
        Assert.assertEquals(1.0, value.getDoughRatio().get(catalog.getDoughList().get(0)), 0.0);
        Assert.assertEquals(0.5, value.getFlavorRatio().get(catalog.getFlavorList().get(0)), 0.0);
        Assert.assertEquals(0.5, value.getFlavorRatio().get(catalog.getFlavorList().get(1)), 0.0);
        Assert.assertEquals(0.25, value.getToppingRatio().get(catalog.getToppingList().get(0)), 0.0);
        Assert.assertEquals(0.25, value.getToppingRatio().get(catalog.getToppingList().get(1)), 0.0);
        Assert.assertEquals(0.25, value.getToppingRatio().get(catalog.getToppingList().get(2)), 0.0);
        Assert.assertEquals(0.25, value.getToppingRatio().get(catalog.getToppingList().get(3)), 0.0);
        Assert.assertEquals(1.0, value.getMixRatio().get(catalog.getMixList().get(1)), 0.0);
        Assert.assertEquals(0.5, value.getCookingRatio().get(catalog.getCookingList().get(0)), 0.0);
        Assert.assertEquals(0.5, value.getCookingRatio().get(catalog.getCookingList().get(1)), 0.0);
    }

    @Test
    public void weightedStatWithMultipleCookies() {
        Order order1 = new Order(this.store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(1),
                Arrays.asList(catalog.getToppingList().get(2), catalog.getToppingList().get(3)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                true), 1);
        order1.addCookie(new Recipe("Created recipe",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0), catalog.getToppingList().get(1)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(0),
                true), 3);
        this.store.placeOrder(order1);
        WeightedIngredientCustomStat stat = new WeightedIngredientCustomStat(this.store);
        IngredientRatio value = stat.computeValue();

        Assert.assertEquals(1, value.getDoughRatio().size());
        Assert.assertEquals(2, value.getFlavorRatio().size());
        Assert.assertEquals(4, value.getToppingRatio().size());
        Assert.assertEquals(1, value.getMixRatio().size());
        Assert.assertEquals(2, value.getCookingRatio().size());
        Assert.assertEquals(1.0, value.getDoughRatio().get(catalog.getDoughList().get(0)), 0.0);
        Assert.assertEquals(0.75, value.getFlavorRatio().get(catalog.getFlavorList().get(0)), 0.0);
        Assert.assertEquals(0.25, value.getFlavorRatio().get(catalog.getFlavorList().get(1)), 0.0);
        Assert.assertEquals(0.375, value.getToppingRatio().get(catalog.getToppingList().get(0)), 0.0);
        Assert.assertEquals(0.375, value.getToppingRatio().get(catalog.getToppingList().get(1)), 0.0);
        Assert.assertEquals(0.125, value.getToppingRatio().get(catalog.getToppingList().get(2)), 0.0);
        Assert.assertEquals(0.125, value.getToppingRatio().get(catalog.getToppingList().get(3)), 0.0);
        Assert.assertEquals(1.0, value.getMixRatio().get(catalog.getMixList().get(1)), 0.0);
        Assert.assertEquals(0.75, value.getCookingRatio().get(catalog.getCookingList().get(0)), 0.0);
        Assert.assertEquals(0.25, value.getCookingRatio().get(catalog.getCookingList().get(1)), 0.0);
    }

    @Test
    public void pickUpTimeCountStatSimple() {
        Store mockStore = mock(Store.class);
        // list with 2 16:00, 1 6:00, 1 23:00, 2 10:30, 1 14:30
        List<LocalTime> times = Arrays.asList(
                LocalTime.of(16, 10),
                LocalTime.of(16, 15),
                LocalTime.of(6, 15),
                LocalTime.of(23, 0),
                LocalTime.of(10, 34),
                LocalTime.of(10, 55),
                LocalTime.of(14, 46)
        );
        // map matching the list
        HashMap<LocalTime, Integer> expectedtimeCounts = new HashMap<>();
        expectedtimeCounts.put(LocalTime.of(16, 0), 2);
        expectedtimeCounts.put(LocalTime.of(6, 0), 1);
        expectedtimeCounts.put(LocalTime.of(23, 0), 1);
        expectedtimeCounts.put(LocalTime.of(10, 30), 2);
        expectedtimeCounts.put(LocalTime.of(14, 30), 1);
        LocalDate date = LocalDate.now();
        List<Order> orders = times.stream().map(time -> new Order(mockStore, LocalDateTime.of(date, time))).collect(Collectors.toList());
        when(mockStore.getOrders()).thenReturn(orders);

        PickUpTimeCountStat stat = new PickUpTimeCountStat(mockStore);
        stat.computeValue();
        Map<LocalTime, Integer> result = stat.getStat();
        Assert.assertEquals(expectedtimeCounts, result);
    }
}