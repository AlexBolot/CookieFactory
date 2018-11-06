package recipe;

import java.util.Collection;

public class Recipe {

    Dough dough;
    Collection<Flavor> flavors;
    Collection<Topping> toppings;
    Mix mix;
    Cooking cooking;
    float price;

    public Recipe(Dough dough, Collection<Flavor> flavors, Collection<Topping> toppings, Mix mix, Cooking cooking, float price) {
        this.dough = dough;
        this.flavors = flavors;
        this.toppings = toppings;
        this.mix = mix;
        this.cooking = cooking;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }
}