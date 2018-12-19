package store;

import api.UnFaithPassAPI;
import main.CookieFirm;
import main.Guest;
import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import recipe.Recipe;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.getFixedClock;

public class UnFaithPassTest {
    private Store store;
    private final CookieFirm cookieFirm = CookieFirm.instance();
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
    public void basicRewardCount() {
        Map<Recipe, Reward> rewards = new HashMap<>();
        Reward reward1 = new Reward(false, 5);
        Reward reward2 = new Reward(true, 1);
        rewards.put(cookieFirm.getGlobalRecipes().get(0), reward1);
        rewards.put(cookieFirm.getGlobalRecipes().get(2), reward2);
        this.store.applyUnFaithPathProgram(new UnFaithPassProgram(rewards));

        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(cookieFirm.getGlobalRecipes().get(0), 3);
        order1.addCookie(cookieFirm.getGlobalRecipes().get(1), 5);
        order1.addCookie(cookieFirm.getGlobalRecipes().get(2), 2);

        UnFaithPassAPI unFaithPassAPI = new UnFaithPassAPI("","");
        this.store.collectRewards(order1,unFaithPassAPI);

        Assert.assertEquals(15,unFaithPassAPI.getPoints());
        Assert.assertEquals(2,unFaithPassAPI.getFreeCookies());

    }

    @Test
    public void setRewardValueToCashRatio_Valid() {
        UnFaithPassProgram pass = new UnFaithPassProgram(new HashMap<>());

        assertEquals(1, pass.getCashFromRewardValue(1), 0.001);

        pass.setRewardValueToCashRatio(2);

        assertEquals(2, pass.getCashFromRewardValue(1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setRewardValueToCashRatio_Negative() {
        new UnFaithPassProgram(new HashMap<>()).setRewardValueToCashRatio(-1);
    }

    @Test
    public void getCashFromRewardValue_Valid() {
        double rewardValue = 10;
        double ratio = 5;

        UnFaithPassProgram pass = new UnFaithPassProgram(new HashMap<>(), ratio);
        assertEquals(rewardValue * ratio, pass.getCashFromRewardValue(rewardValue), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCashFromRewardValue_Negative() {
        new UnFaithPassProgram(new HashMap<>()).getCashFromRewardValue(-5);
    }
}