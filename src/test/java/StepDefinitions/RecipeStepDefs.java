package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import recipe.Recipe;
import recipe.ingredient.*;
import store.Store;
import utils.CucumberContext;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class RecipeStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private final Map<String, Recipe> recipes = new HashMap<>();
    private final Catalog catalog = new Catalog();

    @Given("^\"([^\"]*)\" add the recipe named \"([^\"]*)\" have \"([^\"]*)\", flavor \"([^\"]*)\", topping \"([^\"]*)\", mix \"([^\"]*)\", cooking \"([^\"]*)\"$")
    public void bobAddTheRecipeNamedHaveFlavorToppingMixCooking(String manager, String recipeName, String dough,
                                                                String flavor,
                                                                String topping, String mix, String cooking) throws Throwable {
    }

    @Given("^\"([^\"]*)\" add the recipe named \"([^\"]*)\" have \"([^\"]*)\", flavor \"([^\"]*)\", topping \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\", mix \"([^\"]*)\", cooking \"([^\"]*)\"$")
    public void addTheRecipeNamedHaveFlavorToppingAndAndMixCooking(String manager, String recipeName, String dough,
                                                                   String flavor,
                                                                   String topping,
                                                                   String topping2,
                                                                   String topping3,
                                                                   String mix,
                                                                   String cooking) throws Throwable {
        context.getFacade().managerAddMonthlyCookie(manager, recipeName, dough, flavor, topping, topping2, topping3,
                mix,
                cooking);

    }



    @Then("^The monthly recipe of the \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theMonthlyRecipeOfTheIs(String store, String cookie) throws Throwable {
        Optional<Store> store1 = context.cookieFirm().findStore(store);
        if(store1.isPresent())
            assertEquals(store1.get().getMonthlyRecipe().getName(), cookie);
    }


}
