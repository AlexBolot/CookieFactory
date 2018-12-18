package recipe.ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    /**
     * Add a topping to the list
     * If a topping with the same name already exists it will return this topping ( case insensitive ).
     *
     * @param toppingName {@link String} toppings name
     * @return Topping the created topping, or the already existing one
     */
    public Topping addTopping(String toppingName) {
        Optional<Topping> alreadyExists = toppingList.stream().filter(topping -> topping.getName().equalsIgnoreCase(toppingName)).findFirst();
        Topping newTopping = new Topping(toppingName);
        if (!alreadyExists.isPresent()) {
            toppingList.add(newTopping);
        }
        return alreadyExists.orElse(newTopping);
    }

    /**
     * Add a dough to the list
     * If a dough with the same name already exists it will return this dough ( case insensitive ).
     *
     * @param doughName {@link String} doughs name
     * @return Dough the created dough, or the already existing one
     */
    public Dough addDough(String doughName) {
        Optional<Dough> alreadyExists = doughList.stream().filter(dough -> dough.getName().equalsIgnoreCase(doughName)).findFirst();
        Dough newdough = new Dough(doughName);
        if (!alreadyExists.isPresent()) {
            doughList.add(newdough);
        }
        return alreadyExists.orElse(newdough);
    }

    /**
     * Add a flavor to the list
     * If a flavor with the same name already exists it will return this flavor ( case insensitive ).
     *
     * @param flavorName {@link String} flavors name
     * @return Flavor the created flavor, or the already existing one
     */
    public Flavor addFlavor(String flavorName) {
        Optional<Flavor> alreadyExists = flavorList.stream().filter(flavor -> flavor.getName().equalsIgnoreCase(flavorName)).findFirst();
        Flavor newflavor = new Flavor(flavorName);
        if (!alreadyExists.isPresent()) {
            flavorList.add(newflavor);
        }
        return alreadyExists.orElse(newflavor);
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
