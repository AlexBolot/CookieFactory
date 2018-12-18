package store;

import recipe.Recipe;
import recipe.ingredient.Ingredient;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Manager {

    private Store store;
    private String name;

    public Manager(Store store, String name) {
        this.store = store;
        this.name = name;
    }

    public void changeOpeningTime(DayOfWeek day, LocalTime time) {
        store.setOpeningTime(day, time);
    }

    public void changeClosingTime(DayOfWeek day, LocalTime time) {
        store.setClosingTime(day, time);
    }

    public void changeMontlyRecipe(Recipe recipe) {
        store.setMonthlyRecipe(recipe);
    }

    public void changeIngredientMargin(Ingredient ingredient, double margin) {
        store.getKitchen().setMarginOf(ingredient,margin);
    }

    public void changeRewardPointsToValueRatio(double ratio){
        this.store.getUnFaithPass().setRewardValueToCashRatio(ratio);
    }

    /**
     * change the manager's store suplier price for the given ingredient.
     *
     * @param ingredient {@link Ingredient} target ingredient
     * @param price      {@link int} price, should not be negative
     * @throws IllegalArgumentException for negative price
     */
    public void changeIngredientSupplierPrice(Ingredient ingredient, double price) {
        store.getKitchen().setSupplierPriceOf(ingredient, price);
    }
    public Store getStore() {
        return store;
    }

    public String getName(){return name;}



}