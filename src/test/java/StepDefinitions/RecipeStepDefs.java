package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import recipe.ingredient.Catalog;
import recipe.ingredient.Ingredient;
import store.Store;
import utils.CucumberContext;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class RecipeStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @Given("^\"([^\"]*)\" add the recipe named \"([^\"]*)\" have \"([^\"]*)\", flavor \"([^\"]*)\", topping \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\", mix \"([^\"]*)\", cooking \"([^\"]*)\"$")
    public void addTheRecipeNamedHaveFlavorToppingAndAndMixCooking(String manager,
                                                                   String recipeName,
                                                                   String dough,
                                                                   String flavor,
                                                                   String topping,
                                                                   String topping2,
                                                                   String topping3,
                                                                   String mix,
                                                                   String cooking) {
        context.getFacade().managerAddMonthlyCookie(manager, recipeName, dough, flavor, topping, topping2, topping3, mix, cooking);
    }


    @Then("^The monthly recipe of the \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theMonthlyRecipeOfTheIs(String store, String cookie) {
        Optional<Store> store1 = context.cookieFirm().findStore(store);
        store1.ifPresent(store2 -> assertEquals(store2.getMonthlyRecipe().getName(), cookie));
    }

}
