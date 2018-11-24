package recipe;

import recipe.ingredient.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private String name;
    private Dough dough;
    private Flavor flavor;
    private List<Topping> toppings;
    private Mix mix;
    private Cooking cooking;
    public float price;
    public boolean isCustom;

    public Recipe(String name, Dough dough, Flavor flavor, List<Topping> toppings, Mix mix, Cooking cooking, boolean custom) {
        this.name = name;
        this.dough = dough;
        this.mix = mix;
        this.cooking = cooking;
        this.flavor = flavor;
        setToppings(toppings);
        this.isCustom = custom;
    }

    public void setToppings(List<Topping> toppings) {
        if (toppings.size() > 3)
            throw new IllegalArgumentException("To much toppings!!!");
        else
            this.toppings = toppings;
    }

    public void setDough(Dough dough) {
        this.dough = dough;
    }

    public void setFlavor(Flavor flavor) {
        this.flavor = flavor;
    }

    public void setCooking(Cooking cooking) {
        this.cooking = cooking;
    }

    public void setMix(Mix mix) {
        this.mix = mix;
    }

    public Dough getDough() {
        return dough;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public Mix getMix() {
        return mix;
    }

    public Cooking getCooking() {
        return cooking;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public boolean isCustom() {
        return isCustom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Float.compare(recipe.price, price) == 0 &&
                isCustom == recipe.isCustom &&
                Objects.equals(name, recipe.name) &&
                Objects.equals(dough, recipe.dough) &&
                Objects.equals(flavor, recipe.flavor) &&
                Objects.equals(toppings, recipe.toppings) &&
                Objects.equals(mix, recipe.mix) &&
                Objects.equals(cooking, recipe.cooking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dough, flavor, toppings, mix, cooking, price, isCustom);
    }

    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>(Arrays.asList(this.dough, this.flavor));
        if (!this.getToppings().isEmpty()) ingredients.addAll(this.getToppings());
        return ingredients;
    }
}