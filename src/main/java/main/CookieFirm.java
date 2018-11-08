package main;

import ingredient.*;

import java.util.List;

public class CookieFirm {

    List<Guest> guests;
    List<Store> stores;
    List<Manager> managers;
    List<Recipe> globalRecipes;


    public List<Store> getStores() {
        return stores;
    }

    /**
     * @param cooking
     * @param mix
     * @param dough
     * @param flavors
     * @param toppings
     */
    public Recipe chooseMonthlyRecipe(String name, Cooking cooking, Mix mix, Dough dough, List<Flavor> flavors, List<Topping> toppings, float price, Store store) {
        Recipe newMonthlyRecipe = new Recipe(name, dough, flavors, toppings, mix, cooking, price);
        store.setMonthlyRecipe(newMonthlyRecipe);
        return newMonthlyRecipe;
    }

    /**
     * @param recipe
     */
    private boolean checkRecipeExists(Recipe recipe) {
        return globalRecipes.contains(recipe);
    }

}