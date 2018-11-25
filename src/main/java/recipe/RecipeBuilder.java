package recipe;

import recipe.ingredient.*;

import java.util.List;

public class RecipeBuilder {
    private Catalog catalog = new Catalog();

    /**
     * Create a recipe from ingredients and types of preparation with the certification that they all exists in the catalog
     *
     * @param dough
     * @param flavor
     * @param toppings
     * @param mix
     * @param cooking
     * @return
     */
    public Recipe createRecipe(Dough dough, Flavor flavor, List<Topping> toppings, Mix mix, Cooking cooking) {
        for (Topping topping : toppings) {
            if (!this.catalog.getToppingList().contains(topping)) {
                throw new IllegalArgumentException("This topping does not exist in the catalog");
            }
        }
        if (!this.catalog.getDoughList().contains(dough)) {
            throw new IllegalArgumentException("This dough does not exist in the catalog");
        }
        if (!this.catalog.getFlavorList().contains(flavor)) {
            throw new IllegalArgumentException("This flavor does not exist in the catalog");
        }
        if (!this.catalog.getMixList().contains(mix)) {
            throw new IllegalArgumentException("This mix does not exist in the catalog");
        }
        if (!this.catalog.getCookingList().contains(cooking)) {
            throw new IllegalArgumentException("This cooking does not exist in the catalog");
        }
        //TODO gérer le prix de la recette custom correctement
        return new Recipe("My Recipe", dough, flavor, toppings, mix, cooking, true);
    }
}