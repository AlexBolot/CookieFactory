package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.*;
import main.Manager;
import main.Recipe;
import main.Store;
import main.TestUtils;
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
    private TestUtils utils = new TestUtils();

    @Given("^\"([^\"]*)\" a store$")
    public void aStore(String storeName) {
        stores.put(storeName, new Store(null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(),15));
    }

    @Given("^\"([^\"]*)\" the Manager of \"([^\"]*)\"$")
    public void theManagerOf(String managerName, String storeName) {
        Manager manager = new Manager(stores.get(storeName));
        managers.put(managerName, manager);
    }

    @Given("^A recipe \"([^\"]*)\" is created by \"([^\"]*)\" for \"([^\"]*)\" dollars$")
    public void aRecipeIsCreatedBy(String recipeName, String managerName, String recipePrice) {
        recipes.put(recipeName,new Recipe(recipeName,null,null,new ArrayList<Topping>(),
                null,null,Float.parseFloat(recipePrice)));
    }

    @And("^The dough of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theDoughOfIs(String recipeName, String doughName) {
        Dough dough = utils.doughFromName(doughName);
        if (dough!=null) {
            recipes.get(recipeName).setDough(dough);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The flavor of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theFlavorOfIs(String recipeName, String flavorName) {
        Flavor flavor = utils.flavorFromName(flavorName);
        if (flavor!=null) {
            recipes.get(recipeName).setFlavor(flavor);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The topping of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void laToppingOfIs(String recipeName, String toppingName) {
        Topping topping = utils.toppingFromName(toppingName);
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
        Mix mix = utils.mixFromName(mixName);
        if (mix!=null) {
            recipes.get(recipeName).setMix(mix);
        } else {
            throw new IllegalArgumentException("This ingredient doesn't exists");
        }
    }

    @And("^The cooking of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theCookingOfIs(String recipeName, String cookingName) {
        Cooking cooking = utils.cookingFromName(cookingName);
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
