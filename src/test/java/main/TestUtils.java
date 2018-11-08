package main;

import ingredient.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class TestUtils {

    Recipe randomRecipe() {
        Random random = new Random();
        ArrayList<Topping> toppings = new ArrayList<>();
        ArrayList<Flavor> flavors = new ArrayList<>();

        for (int i = 0; i < random.nextInt(3); i++) {
            toppings.add(new Topping(randomString() + i));
        }

        for (int i = 0; i < random.nextInt(3); i++) {
            flavors.add(new Flavor(randomString() + i));
        }

        Mix mix = random.nextBoolean() ? Mix.MIXED : Mix.TOPPED;
        Cooking cooking = random.nextBoolean() ? Cooking.CHEWY : Cooking.CRUNCHY;

        return new Recipe(randomString(), new Dough(randomString()), flavors, toppings, mix, cooking, random.nextFloat() * 10);
    }

    String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

}
