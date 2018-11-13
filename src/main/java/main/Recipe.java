package main;

import ingredient.*;

import java.util.List;

public class Recipe {

    private String name;
    private Dough dough;
    private List<Flavor> flavors;
    private List<Topping> toppings;
    private Mix mix;
    private Cooking cooking;
    float price;

    public Recipe(String name, Dough dough, List<Flavor> flavors, List<Topping> toppings, Mix mix, Cooking cooking, float price) {
        this.name = name;
        this.dough = dough;
        this.mix = mix;
        this.cooking = cooking;
        setFlavors(flavors);
        setToppings(toppings);
        this.price = price;
    }

    public void setFlavors(List<Flavor> flavors) {
        if (flavors.size() > 3)
            throw new IllegalArgumentException("To much flavors!!!");
        else
            this.flavors = flavors;
    }

    public void setToppings(List<Topping> toppings) {
        if (flavors.size() > 3)
            throw new IllegalArgumentException("To much toppings!!!");
        else
            this.toppings = toppings;
    }

    public void setDough(Dough dough) {
        this.dough = dough;
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

    public List<Flavor> getFlavors() {
        return flavors;
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