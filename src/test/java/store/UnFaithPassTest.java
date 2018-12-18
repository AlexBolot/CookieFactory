package store;

import main.CookieFirm;
import main.Guest;
import order.Order;
import org.junit.Before;
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

    @Test(expected = IllegalStateException.class)
    public void getRewardsWithoutUnFaithPass() {
        Order order1 = new Order(this.store, testingTime.plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(cookieFirm.getGlobalRecipes().get(0), 1);
        store.getRewards(order1);
    }

    @Test
    public void basicRewardCount() {
        Map<Recipe, Reward> rewards = new HashMap<>();
        Reward reward1 = new Reward(false, 5);
        Reward reward2 = new Reward(true, 0);
        rewards.put(cookieFirm.getGlobalRecipes().get(0), reward1);
        rewards.put(cookieFirm.getGlobalRecipes().get(2), reward2);
        this.store.applyUnFaithPath(new UnFaithPass(rewards));

        Order order1 = new Order(this.store, LocalDateTime.now().plusHours(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        order1.setGuest(new Guest());
        order1.addCookie(cookieFirm.getGlobalRecipes().get(0), 3);
        order1.addCookie(cookieFirm.getGlobalRecipes().get(1), 5);
        order1.addCookie(cookieFirm.getGlobalRecipes().get(2), 1);

        List<Reward> aquiredRewards = this.store.getRewards(order1);

        assertEquals(4, aquiredRewards.size());
        assertEquals(reward1, aquiredRewards.get(2));
        assertEquals(reward2, aquiredRewards.get(3));

    }

    @Test
    public void setRewardValueToCashRatio_Valid() {
        UnFaithPass pass = new UnFaithPass(new HashMap<>());

        assertEquals(1, pass.getCashFromRewardValue(1), 0.001);

        pass.setRewardValueToCashRatio(2);

        assertEquals(2, pass.getCashFromRewardValue(1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setRewardValueToCashRatio_Negative() {
        new UnFaithPass(new HashMap<>()).setRewardValueToCashRatio(-1);
    }

    @Test
    public void getCashFromRewardValue_Valid() {
        double rewardValue = 10;
        double ratio = 5;

        UnFaithPass pass = new UnFaithPass(new HashMap<>(), ratio);
        assertEquals(rewardValue * ratio, pass.getCashFromRewardValue(rewardValue), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCashFromRewardValue_Negative() {
        new UnFaithPass(new HashMap<>()).getCashFromRewardValue(-5);
    }
}
