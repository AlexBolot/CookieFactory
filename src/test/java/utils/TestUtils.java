package utils;

import ingredient.*;
import main.Day;
import main.Recipe;

import java.util.ArrayList;
import java.util.Random;

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
}
