package main;

import ingredient.*;

import java.util.List;

public class Recipe {

    private String name;
    private Dough dough;
    private Flavor flavor;
    private List<Topping> toppings;
    private Mix mix;
    private Cooking cooking;
    float price;

    public Recipe(String name, Dough dough, Flavor flavor, List<Topping> toppings, Mix mix, Cooking cooking, float price) {
        this.name = name;
        this.dough = dough;
        this.mix = mix;
        this.cooking = cooking;
        this.flavor = flavor;
        setToppings(toppings);
        this.price = price;
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
}