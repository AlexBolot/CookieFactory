package main;

import recipe.*;

import java.util.List;

public class Kitchen {

    private List<Dough> doughList;
    private List<Flavor> flavorList;
    private List<Topping> toppingList;
    private List<Mix> mixList;
    private List<Cooking> cookingList;

    public Kitchen(List<Dough> doughs, List<Flavor> flavors, List<Topping> toppings, List<Mix> mixes, List<Cooking> cookings) {
        this.doughList = doughs;
        this.flavorList = flavors;
        this.toppingList = toppings;
        this.mixList = mixes;
        this.cookingList = cookings;
    }

    public List<Dough> getDoughList() {
        return doughList;
    }

    public List<Flavor> getFlavorList() {
        return flavorList;
    }

    public List<Topping> getToppingList() {
        return toppingList;
    }

    public List<Mix> getMixList() {
        return mixList;
    }

    public List<Cooking> getCookingList() {
        return cookingList;
    }
}
