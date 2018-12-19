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
    private Map<Ingredient, Double> supplierPrices;
    private Map<Ingredient, Double> margins;

    public Kitchen() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    Kitchen(Map<Ingredient, Integer> stock, Map<Ingredient, Double> supplierPrices, Map<Ingredient, Double> margins) {
        stock.forEach((key, value) -> {
            if (value <= 0)
                throw new IllegalArgumentException("Amount of " + key.getName() + " must be strictly positive. Given is " + value);
        });

        supplierPrices.forEach((key, value) -> {
            if (value <= 0)
                throw new IllegalArgumentException("Supplier price for " + key.getName() + " must be strictly positive. Given is " + value);
        });

        margins.forEach((key, value) -> {
            if (value <= 0)
                throw new IllegalArgumentException("Margin for " + key.getName() + " must be strictly positive. Given is " + value);
        });

        this.stock = stock;
        this.supplierPrices = supplierPrices;
        this.margins = margins;
    }

    /**
     * Tells if the Kitchen has all required getIngredients and quantities to cook a given [recipe]
     *
     * @param recipe Recipe to check
     * @return True if stock allows to cook [recipe], False otherwise
     */
    public boolean canDo(Recipe recipe, int amount) {
        return recipeCapacity(recipe) >= amount;
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
    void cook(Recipe recipe, int amount) {
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

    /**
     * The price of a given [ingredient], calculated from the supplier's price + the margin
     *
     * @param ingredient Ingredient to look for
     * @return The price of a given [ingredient], calculated from the supplier's price + the margin
     */
    public double vendingPriceOf(Ingredient ingredient) {

        if(!supplierPrices.containsKey(ingredient)) throw new IllegalArgumentException("Unknown supplier price for " + ingredient.getName());
        if(!margins.containsKey(ingredient)) throw new IllegalArgumentException("Unknown margin for " + ingredient.getName());

        double supplierPrice = supplierPrices.get(ingredient);
        double marginPercent = margins.get(ingredient);

        double marginValue = supplierPrice * (marginPercent / 100);
        return supplierPrice + marginValue;
    }

    /**
     * Sets the supplier price of an ingredient. Price should not be negative
     *
     * @param ingredient {@link Ingredient} target ingredient
     * @param price      double desired price
     */
    public void setSupplierPriceOf(Ingredient ingredient, double price) {
        if (price < 0)
            throw new IllegalArgumentException("Price should not be negative");
        supplierPrices.put(ingredient, price);
    }

    /**
     * Sets the margin amount for a given [ingredient]
     *
     * @param ingredient Ingredient to put margin onto
     * @param margin Percent of the supplierPrice to add, to obtain vendingPrice
     */
    public void setMarginOf(Ingredient ingredient, double margin) {
        margins.put(ingredient, margin);
    }

    public double getMarginOf(Ingredient ingredient) {return margins.get(ingredient);}

    public Map<Ingredient, Double> getMargins() {
        return margins;
    }
}
