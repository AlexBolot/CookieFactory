package main;

import ingredient.Dough;
import ingredient.Flavor;
import ingredient.Ingredient;
import ingredient.Topping;

import java.util.HashMap;
import java.util.Map;

/**
 * The Kitchen represents the stock of Ingredients of a Store
 */
public class Kitchen {

    private Map<Ingredient, Integer> stock;

    public Kitchen() {
        stock = new HashMap<>();
    }

    public Kitchen(Map<Ingredient, Integer> stock) {

        stock.forEach((key, value) -> {
            if (value <= 0)
                throw new IllegalArgumentException("Amount of " + key.getName() + " must be strictly positive. Given is " + value);
        });

        this.stock = stock;
    }

    /**
     * Tells if the Kitchen has all required ingredients and quantities to cook a given [recipe]
     *
     * @param recipe Recipe to check
     * @return True if stock allows to cook [recipe], False otherwise
     */
    public boolean canDo(Recipe recipe) {

        Map<Ingredient, Integer> required = new HashMap<>();

        for (Topping topping : recipe.getToppings()) {
            if (required.containsKey(topping))
                required.replace(topping, required.get(topping) + 1);
            else
                required.put(topping, 1);
        }

        Dough dough = recipe.getDough();
        if (!stock.containsKey(dough) || stock.get(dough) < 1) return false;
        Flavor flavor = recipe.getFlavor();
        if (flavor != null && (!stock.containsKey(flavor) || stock.get(flavor) < 1)) return false;

        return required.entrySet().stream().allMatch(entry -> hasInStock(entry.getKey(), entry.getValue()));
    }

    /**
     * Tells if the Kitchen has the given [ingredient] and if yes, if it has at least the given [amount]
     *
     * @param ingredient Ingredient to find in the stock
     * @param amount     Minimal requied amount to be found in the stock (must be strictly positive)
     * @return True if stock contains [ingredient], with amount of [amount], False otherwise
     */
    public boolean hasInStock(Ingredient ingredient, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        return stock.containsKey(ingredient) && stock.get(ingredient) >= amount;
    }

    /**
     * Cosumes the requiered Flavors, Toppings and Dough of the given [recipe], as many times as [amount]
     *
     * @param recipe Recipe to cook with ingredients from the Kitchen
     * @param amount How many times do we cook [recipe] (must be stricly positive)
     */
    public void cook(Recipe recipe, int amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        for (int i = 0; i < amount; i++) {

            if (!canDo(recipe))
                throw new IllegalArgumentException("Can not prepare recipe " + recipe.getName() + " : missing ingredients");

            for (Topping topping : recipe.getToppings()) {
                stock.replace(topping, stock.get(topping) - 1);
            }

            Dough dough = recipe.getDough();
            stock.replace(dough, stock.get(dough) - 1);

            Flavor flavor = recipe.getFlavor();
            if (flavor != null) {
                stock.replace(flavor, stock.get(flavor) - 1);
            }
        }
    }

    /**
     * Updates the [amount] of [ingredient] in the stock
     *
     * @param ingredient Ingredient being refilled
     * @param amount     Amount of [ingredient] to add to the stock (must be strictly positive)
     */
    public void refill(Ingredient ingredient, int amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        if (stock.containsKey(ingredient))
            stock.replace(ingredient, stock.get(ingredient) + amount);
        else
            stock.put(ingredient, amount);
    }

}
