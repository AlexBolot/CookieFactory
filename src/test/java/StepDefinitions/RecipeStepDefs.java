package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.*;
import main.Manager;
import main.Recipe;
import main.Store;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeStepDefs {

    private Map<String, Manager> managers = new HashMap<>();
    private Map<String, Store> stores = new HashMap<>();
    private Map<String, Recipe> recipes = new HashMap<>();
    private Catalog catalog = new Catalog();

    private Mix mixFromName(String mixName) {
        for (Mix mix : catalog.getMixList()) {
            if (mix.getName().equalsIgnoreCase(mixName)) return mix;
        }
        return null;
    }
    private Cooking cookingFromName(String cookingName) {
        for (Cooking cooking : catalog.getCookingList()) {
            if (cooking.getName().equalsIgnoreCase(cookingName)) return cooking;
        }
        return null;
    }
    private Dough doughFromName(String doughName) {
        for (Dough dough : catalog.getDoughList()) {
            if (dough.getName().equalsIgnoreCase(doughName)) return dough;
        }
        return null;
    }
    private Topping toppingFromName(String toppingName) {
        for (Topping topping : catalog.getToppingList()) {
            if (topping.getName().equalsIgnoreCase(toppingName)) return topping;
        }
        return null;
    }
    private Flavor flavorFromName(String flavorName) {
        for (Flavor flavor : catalog.getFlavorList()) {
            if (flavor.getName().equalsIgnoreCase(flavorName)) return flavor;
        }
        return null;
    }

    @Given("^\"([^\"]*)\" a store$")
    public void aStore(String storeName) {
        stores.put(storeName, new Store(15));
    }

    @Given("^\"([^\"]*)\" the Manager of \"([^\"]*)\"$")
    public void theManagerOf(String managerName, String storeName) {
        Manager manager = new Manager(stores.get(storeName));
        managers.put(managerName, manager);
    }

    @Given("^A recipe \"([^\"]*)\" is created by \"([^\"]*)\" for \"([^\"]*)\" dollars$")
    public void aRecipeIsCreatedBy(String recipeName, String managerName, String recipePrice) {
        recipes.put(recipeName,new Recipe(recipeName,null,new ArrayList<Flavor>(),new ArrayList<Topping>(),
                null,null,Float.parseFloat(recipePrice)));
    }

    @And("^The dough of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theDoughOfIs(String recipeName, String doughName) {
        Dough dough = doughFromName(doughName);
        if (dough!=null) {
            recipes.get(recipeName).setDough(dough);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The flavor of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theFlavorOfIs(String recipeName, String flavorName) {
        Flavor flavor = flavorFromName(flavorName);
        if (flavor!=null) {
            List<Flavor> flavors = new ArrayList<>();
            flavors.add(flavor);
            recipes.get(recipeName).setFlavors(flavors);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The topping of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void laToppingOfIs(String recipeName, String toppingName) {
        Topping topping = toppingFromName(toppingName);
        if (topping!=null) {
            List<Topping> toppings = new ArrayList<>();
            toppings.add(topping);
            recipes.get(recipeName).setToppings(toppings);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The mix of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theMixOfIs(String recipeName, String mixName) {
        Mix mix = mixFromName(mixName);
        if (mix!=null) {
            recipes.get(recipeName).setMix(mix);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The cooking of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theCookingOfIs(String recipeName, String cookingName) {
        Cooking cooking = cookingFromName(cookingName);
        if (cooking!=null) {
            recipes.get(recipeName).setCooking(cooking);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @When("^\"([^\"]*)\" add \"([^\"]*)\" as monthly recipe$")
    public void addAsMonthlyRecipe(String managerName, String recipeName) {
        managers.get(managerName).changeMontlyRecipe(recipes.get(recipeName));
    }

    @Then("^The monthly recipe of the store of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theMonthlyRecipeOfTheStoreOfIs(String managerName, String recipeName) {
        Recipe recipe = managers.get(managerName).getStore().getMonthlyRecipe();
        Assert.assertEquals(recipes.get(recipeName),recipe);
    }
}
