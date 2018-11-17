package main;

import ingredient.Dough;
import ingredient.Flavor;
import ingredient.Ingredient;
import ingredient.Topping;

import java.util.HashMap;
import java.util.Map;

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

    public boolean canDo(Recipe recipe) {

        Map<Ingredient, Integer> required = new HashMap<>();

        for (Flavor flavor : recipe.getFlavors()) {
            if (required.containsKey(flavor))
                required.replace(flavor, required.get(flavor) + 1);
            else
                required.put(flavor, 1);
        }

        for (Flavor flavor : recipe.getFlavors()) {
            if (required.containsKey(flavor))
                required.replace(flavor, required.get(flavor) + 1);
            else
                required.put(flavor, 1);
        }

        Dough dough = recipe.getDough();
        if (!stock.containsKey(dough) || stock.get(dough) < 1) return false;

        return required.entrySet().stream().allMatch(entry -> hasInStock(entry.getKey(), entry.getValue()));
    }

    public boolean hasInStock(Ingredient ingredient, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        return stock.containsKey(ingredient) && stock.get(ingredient) >= amount;
    }

    public void cook(Recipe recipe, int amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        for (int i = 0; i < amount; i++) {

            if (!canDo(recipe))
                throw new IllegalArgumentException("Can not prepare recipe " + recipe.getName() + " : missing ingredients");

            for (Flavor flavor : recipe.getFlavors()) {
                stock.replace(flavor, stock.get(flavor) - 1);
            }

            for (Topping topping : recipe.getToppings()) {
                stock.replace(topping, stock.get(topping) - 1);
            }

            Dough dough = recipe.getDough();
            stock.replace(dough, stock.get(dough) - 1);
        }
    }

    public void refill(Ingredient ingredient, int amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is " + amount);

        if (stock.containsKey(ingredient))
            stock.replace(ingredient, stock.get(ingredient) + amount);
        else
            stock.put(ingredient, amount);
    }

}
