package main;

import recipe.*;

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
     * @param flavor
     * @param topping
     */
    public Recipe chooseMonthlyRecipe(Cooking cooking, Mix mix, Dough dough, Flavor flavor, Topping topping, Store store) {
        Recipe newMonthlyRecipe = new Recipe(cooking, mix, dough, flavor, topping);
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