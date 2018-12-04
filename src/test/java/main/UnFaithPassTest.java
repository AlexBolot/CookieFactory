package main;

import cucumber.api.java.it.Ma;
import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import store.Manager;
import store.Reward;
import store.Store;
import store.UnFaithPass;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnFaithPassTest {
    private final TestUtils utils = new TestUtils();
    private Store store;
    private CookieFirm cookieFirm = CookieFirm.instance();


    @Before
    public void setUp() {
        this.store = new Store("", null, new ArrayList<>(), new HashMap<>(), new HashMap<>(), 0f, 1);
        this.store.setKitchen(TestUtils.getInfiniteMockKitchen());
        Manager manager = new Manager(store, "Bob");
        manager.changeOpeningTime(DayOfWeek.MONDAY, LocalTime.now().minusHours(4));
        manager.changeClosingTime(DayOfWeek.MONDAY, LocalTime.now().plusHours(4));

    }

    @Test (expected = IllegalStateException.class)
    public void getRewardsWithoutUnFaithPass() {
        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(cookieFirm.getGlobalRecipes().get(0),1);
        store.getRewards(order1);
    }

    @Test
    public void basicRewardCount() {
        Map<Recipe, Reward> rewards = new HashMap<>();
        Reward reward1 = new Reward(false,5);
        Reward reward2 = new Reward(true,0);
        rewards.put(cookieFirm.getGlobalRecipes().get(0),reward1);
        rewards.put(cookieFirm.getGlobalRecipes().get(2),reward2);
        this.store.applyUnFaithPath(new UnFaithPass(rewards));

        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.next(DayOfWeek
                .MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(cookieFirm.getGlobalRecipes().get(0),3);
        order1.addCookie(cookieFirm.getGlobalRecipes().get(1),5);
        order1.addCookie(cookieFirm.getGlobalRecipes().get(2),1);

        List<Reward> aquiredRewards = this.store.getRewards(order1);

        Assert.assertEquals(4,aquiredRewards.size());
        Assert.assertEquals(reward1,aquiredRewards.get(2));
        Assert.assertEquals(reward2,aquiredRewards.get(3));

    }
}
