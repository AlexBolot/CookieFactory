package store;

import recipe.Recipe;
import recipe.ingredient.Dough;
import recipe.ingredient.Flavor;
import recipe.ingredient.Ingredient;
import recipe.ingredient.Topping;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
     * Tells if the Kitchen has all required getIngredients and quantities to cook a given [recipe]
     *
     * @param recipe Recipe to check
     * @return True if stock allows to cook [recipe], False otherwise
     */
    public boolean canDo(Recipe recipe, int amount) {

        return recipeCapacity(recipe) >= amount;
        /*

        Map<Ingredient, Integer> required = new HashMap<>();

        for (Topping topping : recipe.getToppings()) {
            if (required.containsKey(topping))
                required.replace(topping, required.get(topping) + amount);
            else
                required.put(topping, amount);
        }

        Dough dough = recipe.getDough();
        if (!stock.containsKey(dough) || stock.get(dough) < amount) return false;
        Flavor flavor = recipe.getFlavor();
        if (flavor != null && (!stock.containsKey(flavor) || stock.get(flavor) < amount)) return false;

        return required.entrySet().stream().allMatch(entry -> hasInStock(entry.getKey(), entry.getValue()));*/
    }

    /**
     * Tells if the Kitchen has the given [recipe.ingredient] and if yes, if it has at least the given [amount]
     *
     * @param ingredient Ingredient to find in the stock
     * @param amount     Minimal requied amount to be found in the stock (must be strictly positive)
     * @return True if stock contains [recipe.ingredient], with amount of [amount], False otherwise
     */
    public boolean hasInStock(Ingredient ingredient, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        return stock.containsKey(ingredient) && stock.get(ingredient) >= amount;
    }

    /**
     * Cosumes the requiered Flavors, Toppings and Dough of the given [recipe], as many times as [amount]
     *
     * @param recipe Recipe to cook with getIngredients from the Kitchen
     * @param amount How many times do we cook [recipe] (must be stricly positive)
     */
    public void cook(Recipe recipe, int amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);


        if (!canDo(recipe, amount))
            throw new IllegalArgumentException("Can not prepare recipe " + recipe.getName() + " : missing getIngredients");

        for (Topping topping : recipe.getToppings()) {
            stock.replace(topping, stock.get(topping) - amount);
        }

        Dough dough = recipe.getDough();
        stock.replace(dough, stock.get(dough) - amount);

        Flavor flavor = recipe.getFlavor();
        if (flavor != null) {
            stock.replace(flavor, stock.get(flavor) - amount);
        }
    }

    /**
     * Updates the [amount] of [recipe.ingredient] in the stock
     *
     * @param ingredient Ingredient being refilled
     * @param amount     Amount of [recipe.ingredient] to add to the stock (must be strictly positive)
     */
    public void refill(Ingredient ingredient, int amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        if (stock.containsKey(ingredient))
            stock.replace(ingredient, stock.get(ingredient) + amount);
        else
            stock.put(ingredient, amount);
    }

    /**
     * How many cookies the kitchen can produce for a given recipe
     *
     * @param recipe Recipe of the expected cookie
     * @return Amount of cookies matching [recipe] the kitchen can produce
     */
    public int recipeCapacity(Recipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients();

        if (!ingredients.stream().allMatch(this.stock::containsKey))
            return 0;

        Map<Ingredient, Integer> required = new HashMap<>();
        ingredients.forEach(item -> required.put(item, Collections.frequency(ingredients, item)));

        return required.entrySet().stream()
                .mapToInt(entry -> this.stock.get(entry.getKey()) / entry.getValue()).min().orElse(0);
    }
}
