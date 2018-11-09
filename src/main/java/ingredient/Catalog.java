package ingredient;

import ingredient.*;

import java.util.ArrayList;
import java.util.List;

public class Catalog {

    private List<Dough> doughList = new ArrayList<>();
    private List<Flavor> flavorList = new ArrayList<>();
    private List<Topping> toppingList= new ArrayList<>();
    private List<Mix> mixList = new ArrayList<>();
    private List<Cooking> cookingList = new ArrayList<>();

    public Catalog() {
        this.doughList.add(new Dough("Oatmeal"));
        this.doughList.add(new Dough("Chocolat"));
        this.flavorList.add(new Flavor("Vanilla"));
        this.flavorList.add(new Flavor("Chili"));
        this.toppingList.add(new Topping("White Chocolat"));
        this.toppingList.add(new Topping("Mnms"));
        this.mixList =  new ArrayList<>();
        this.cookingList =  new ArrayList<>();
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
