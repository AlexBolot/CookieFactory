package recipe.ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represent the {@link main.CookieFirm} getIngredients catalog
 */
public class Catalog {

    private List<Dough> doughList = new ArrayList<>();
    private List<Flavor> flavorList = new ArrayList<>();
    private List<Topping> toppingList= new ArrayList<>();
    private List<Mix> mixList = new ArrayList<>();
    private List<Cooking> cookingList = new ArrayList<>();

    public Catalog() {

        this.doughList.addAll(Arrays.asList(
                new Dough("Oatmeal"),
                new Dough("Plain"),
                new Dough("Chocolate"),
                new Dough("Peanut butter")
        ));
        this.flavorList.addAll(Arrays.asList(
                new Flavor("Vanilla"),
                new Flavor("Cinnamon"),
                new Flavor("Chili"),
                new Flavor("Oyster")
                )
        );

        this.toppingList.addAll(Arrays.asList(
                new Topping("White Chocolate"),
                new Topping("Milk Chocolate"),
                new Topping("Black Chocolate"),
                new Topping("M&m's"),
                new Topping("Reese's buttercup")
        ));
        this.mixList.addAll(Arrays.asList(
                new Mix("Mixed"),
                new Mix("Topped")
        ));
        this.cookingList.addAll(Arrays.asList(
                new Cooking("Crunchy"),
                new Cooking("Chewy")
        ));

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
