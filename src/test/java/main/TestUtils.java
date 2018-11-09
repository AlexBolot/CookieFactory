package main;

import ingredient.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class TestUtils {

    Recipe randomRecipe() {
        Random random = new Random();
        Catalog catalog = new Catalog();
        ArrayList<Topping> toppings = new ArrayList<>();
        ArrayList<Flavor> flavors = new ArrayList<>();

        for (int i = 0; i < random.nextInt(3); i++) {
            toppings.add(catalog.getToppingList().get(1));
        }

        for (int i = 0; i < random.nextInt(3); i++) {
            flavors.add(catalog.getFlavorList().get(1));
        }

        Mix mix = random.nextBoolean() ? Mix.MIXED : Mix.TOPPED;
        Cooking cooking = random.nextBoolean() ? Cooking.CHEWY : Cooking.CRUNCHY;

        return new Recipe(randomString(), catalog.getDoughList().get(1), flavors, toppings, mix, cooking, random.nextFloat() *
                10);
    }

    String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

}
