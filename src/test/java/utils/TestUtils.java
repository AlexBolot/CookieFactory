package utils;

import ingredient.*;
import main.Day;
import main.Kitchen;
import main.Recipe;
import order.OrderState;

import java.util.ArrayList;
import java.util.Random;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    private final Catalog catalog = new Catalog();

    public Recipe randomRecipe() {
        Random random = new Random();
        Catalog catalog = new Catalog();
        ArrayList<Topping> toppings = new ArrayList<>();

        for (int i = 0; i < random.nextInt(3); i++) {
            toppings.add(catalog.getToppingList().get(1));
        }

        Mix mix = catalog.getMixList().get(random.nextBoolean() ? 0 : 1);
        Cooking cooking = catalog.getCookingList().get(random.nextBoolean() ? 0 : 1);

        return new Recipe("randomRecipe", catalog.getDoughList().get(1), catalog.getFlavorList().get(1), toppings, mix, cooking, random.nextFloat() *
                10);
    }

    public Day dayFromName(String dayName) {
        for (Day day : Day.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
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

    public Mix mixFromName(String mixName) {
        for (Mix mix : catalog.getMixList()) {
            if (mix.getName().equalsIgnoreCase(mixName)) return mix;
        }
        return null;
    }

    public Cooking cookingFromName(String cookingName) {
        for (Cooking cooking : catalog.getCookingList()) {
            if (cooking.getName().equalsIgnoreCase(cookingName)) return cooking;
        }
        return null;
    }

    public Dough doughFromName(String doughName) {
        for (Dough dough : catalog.getDoughList()) {
            if (dough.getName().equalsIgnoreCase(doughName)) return dough;
        }
        return null;
    }

    public Topping toppingFromName(String toppingName) {
        for (Topping topping : catalog.getToppingList()) {
            if (topping.getName().equalsIgnoreCase(toppingName)) return topping;
        }
        return null;
    }

    public Flavor flavorFromName(String flavorName) {
        for (Flavor flavor : catalog.getFlavorList()) {
            if (flavor.getName().equalsIgnoreCase(flavorName)) return flavor;
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
    }

    public static Kitchen getInfiniteMockKitchen() {
        final Kitchen kitchen = mock(Kitchen.class);
        when(kitchen.canDo(any())).thenReturn(true);
        when(kitchen.hasInStock(any(), anyInt())).thenReturn(true);
        when(kitchen.recipeCapacity(any())).thenReturn(Integer.MAX_VALUE);
        return kitchen;
    }
}
