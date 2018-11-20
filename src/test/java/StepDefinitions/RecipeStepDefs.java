package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import recipe.Recipe;
import recipe.ingredient.*;
import utils.CucumberContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private final Map<String, Recipe> recipes = new HashMap<>();
    private final Catalog catalog = new Catalog();

    @Given("^A recipe \"([^\"]*)\" is created by \"([^\"]*)\" for \"([^\"]*)\" dollars$")
    public void aRecipeIsCreatedBy(String recipeName, String managerName, String recipePrice) {
        recipes.put(recipeName,new Recipe(recipeName,null,null, new ArrayList<>(),
                null,null,Float.parseFloat(recipePrice)));
    }

    @And("^The dough of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theDoughOfIs(String recipeName, String doughName) {
        Dough dough = context.utils.doughFromName(doughName);
        if (dough!=null) {
            recipes.get(recipeName).setDough(dough);
        } else {
            throw new IllegalArgumentException("This recipe.ingredient doesn't exists");
        }
    }

    @And("^The flavor of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theFlavorOfIs(String recipeName, String flavorName) {
        Flavor flavor = context.utils.flavorFromName(flavorName);
        if (flavor!=null) {
            recipes.get(recipeName).setFlavor(flavor);
        } else {
            throw new IllegalArgumentException("This recipe.ingredient doesn't exists");
        }
    }

    @And("^The topping of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void laToppingOfIs(String recipeName, String toppingName) {
        Topping topping = context.utils.toppingFromName(toppingName);
        if (topping!=null) {
            List<Topping> toppings = new ArrayList<>();
            toppings.add(topping);
            recipes.get(recipeName).setToppings(toppings);
        } else {
            throw new IllegalArgumentException("This recipe.ingredient doesn't exists");
        }
    }

    @And("^The mix of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theMixOfIs(String recipeName, String mixName) {
        Mix mix = context.utils.mixFromName(mixName);
        if (mix!=null) {
            recipes.get(recipeName).setMix(mix);
        } else {
            throw new IllegalArgumentException("This recipe.ingredient doesn't exists");
        }
    }

    @And("^The cooking of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theCookingOfIs(String recipeName, String cookingName) {
        Cooking cooking = context.utils.cookingFromName(cookingName);
        if (cooking!=null) {
            recipes.get(recipeName).setCooking(cooking);
        } else {
            throw new IllegalArgumentException("This recipe.ingredient doesn't exists");
        }
    }

    @When("^\"([^\"]*)\" add \"([^\"]*)\" as monthly recipe$")
    public void addAsMonthlyRecipe(String managerName, String recipeName) {
        context.managers.get(managerName).changeMontlyRecipe(recipes.get(recipeName));
    }

    @Then("^The monthly recipe of the store of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theMonthlyRecipeOfTheStoreOfIs(String managerName, String recipeName) {
        Recipe recipe = context.managers.get(managerName).getStore().getMonthlyRecipe();
        Assert.assertEquals(recipes.get(recipeName),recipe);
    }
}
