package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import recipe.ingredient.Ingredient;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class KitchenStepDef {


    private final CucumberContext context = CucumberContext.getContext();

    @Then("^The kitchen of \"([^\"]*)\" has exactly or more than (\\d+) \"([^\"]*)\" \"([^\"]*)\"$")
    public void theKitchenOfHasExactly(String store, int quantity, String type, String ingredient) {

        Optional<Store> opStore = context.cookieFirm().findStore(store);

        assertTrue(opStore.isPresent());
        assertTrue(opStore.get().getKitchen().hasInStock(context.getFacade().ingredientFromName(type, ingredient), quantity));
    }

    @Given("^The kitchen manage by \"([^\"]*)\" has a margin of (\\d+) for the \"([^\"]*)\" \"([^\"]*)\"$")
    public void theKitchenOfHasAMarginOfForThe(String manager, int margin, String type, String ingredient) {
        context.getFacade().managerChangeIngredientMargin(manager, type, ingredient, (double) margin);
    }

    @When("^\"([^\"]*)\" change the margin of the \"([^\"]*)\" \"([^\"]*)\" to (\\d+)$")
    public void changeTheMarginOfTheTo(String manager, String type, String ingredient, int margin) {
        context.getFacade().managerChangeIngredientMargin(manager, type, ingredient, (double) margin);
    }

    @Then("^The margin of \"([^\"]*)\" for the \"([^\"]*)\" \"([^\"]*)\" has been changed to (\\d+)$")
    public void theMarginOfForTheHasBeenChangedTo(String store, String type, String ingredientName, int margin) {
        Optional<Store> opStore = context.cookieFirm().findStore(store);
        if (opStore.isPresent()) {
            Ingredient ingredient = context.getFacade().ingredientFromName(type, ingredientName);
            Assert.assertEquals((double) margin, opStore.get().getKitchen().getMarginOf(ingredient), 0);
        }
    }

}
