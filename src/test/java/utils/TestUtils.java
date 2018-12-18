package utils;

import api.BankAPI;
import main.CookieFirm;
import order.OrderState;
import recipe.Recipe;
import recipe.ingredient.*;
import store.Kitchen;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class TestUtils {

    private final Catalog catalog = new Catalog();
    private final CookieFirm cookieFirm = CookieFirm.instance();

    public Recipe randomRecipe() {
        Random random = new Random();
        Catalog catalog = new Catalog();
        ArrayList<Topping> toppings = new ArrayList<>();

        for (int i = 0; i < random.nextInt(3); i++) {
            toppings.add(catalog.getToppingList().get(1));
        }

        Mix mix = catalog.getMixList().get(random.nextBoolean() ? 0 : 1);
        Cooking cooking = catalog.getCookingList().get(random.nextBoolean() ? 0 : 1);

        return new Recipe("randomRecipe", catalog.getDoughList().get(1), catalog.getFlavorList().get(1), toppings, mix, cooking,
                true);
    }

    public String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
    }

    public OrderState stateFromName(String targetStateName) {
        OrderState targetState = null;
        switch (targetStateName) {
            case "Draft":
                targetState = OrderState.DRAFT;
                break;
            case "Ordered":
                targetState = OrderState.ORDERED;
                break;
            case "Canceled":
                targetState = OrderState.CANCELED;
                break;
            case "Withdrawn":
                targetState = OrderState.WITHDRAWN;
        }
        return targetState;
    }

    public Recipe recipeFromName(String recipeName) {
        for (Recipe recipe : cookieFirm.getGlobalRecipes()) {
            if (recipe.getName().equals(recipeName)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Fill up the kitchen with enough ingredients for the recipe
     *
     * @param kitchen
     * @param recipe
     * @param amount
     */
    public static void fillKitchenForRecipe(Kitchen kitchen, Recipe recipe, int amount) {
        kitchen.refill(recipe.getDough(), amount);
        kitchen.refill(recipe.getFlavor(), amount);
        recipe.getToppings().forEach(t -> kitchen.refill(t, amount));

        Random random = new Random();

        kitchen.setMarginOf(recipe.getDough(), random.nextInt(10) + 5);
        kitchen.setMarginOf(recipe.getFlavor(), random.nextInt(10) + 5);
        recipe.getToppings().forEach(t -> kitchen.setMarginOf(t, random.nextInt(10) + 5));

        kitchen.setSupplierPriceOf(recipe.getDough(), random.nextInt(10) + 5);
        kitchen.setSupplierPriceOf(recipe.getFlavor(), random.nextInt(10) + 5);
        recipe.getToppings().forEach(t -> kitchen.setSupplierPriceOf(t, random.nextInt(10) + 5));

    }

    public static Kitchen getInfiniteMockKitchen() {
        final Kitchen kitchen = mock(Kitchen.class);
        when(kitchen.canDo(any(), anyInt())).thenReturn(true);
        when(kitchen.hasInStock(any(), anyInt())).thenReturn(true);
        when(kitchen.recipeCapacity(any())).thenReturn(Integer.MAX_VALUE);
        when(kitchen.vendingPriceOf(any())).thenReturn((double) 1);
        return kitchen;
    }

    /**
     * Obtains a clock that always returns the same instant.
     * <p>
     * This clock simply returns the specified instant.
     * The main use case for this is in testing, where the fixed clock ensures
     * tests are not dependent on the current clock.
     *
     * @param dayOfWeek Day of the week to adjust clock
     * @param hour    Hours set to the clock
     * @param minutes Minutes set to the clock
     * @return A clock that always returns the defined instant
     */
    public static Clock getFixedClock(DayOfWeek dayOfWeek, int hour, int minutes) {
        LocalDateTime localTime = LocalDateTime.now().withHour(hour).withMinute(minutes).with(TemporalAdjusters.nextOrSame(dayOfWeek));
        return Clock.fixed(localTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }
    /**
     * Obtains a clock that always returns the same instant.
     * <p>
     * This clock simply returns the specified instant.
     * The main use case for this is in testing, where the fixed clock ensures
     * tests are not dependent on the current clock.
     *
     * @param hour    Hours set to the clock
     * @param minutes Minutes set to the clock
     * @return A clock that always returns the defined instant
     */
    public static Clock getFixedClock(int hour, int minutes) {
        return getFixedClock(LocalDateTime.now().getDayOfWeek(), hour, minutes);
    }

    public static BankAPI lenientBankAPI() {
        final BankAPI bankAPI = mock(BankAPI.class);
        doNothing().when(bankAPI).pay(any(), anyInt());

        return bankAPI;
    }

    public String createEmail(String name) {
        return name + "@" + name + ".fr";
    }

    public boolean payOnline(String pay) {
        return pay.equals("online");
    }
}
