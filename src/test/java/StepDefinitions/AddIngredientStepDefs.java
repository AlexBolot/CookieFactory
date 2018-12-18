package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import recipe.ingredient.Catalog;
import recipe.ingredient.Ingredient;
import utils.CucumberContext;

import java.util.Collection;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;

public class AddIngredientStepDefs {
    private final CucumberContext context = CucumberContext.getContext();

    @Then("^The ingredient catalog contains \"([^\"]*)\"$")
    public void theIngredientCatalogContains(String name) {
        Catalog catalog = context.cookieFirm().getCatalog();
        Stream<Ingredient> allIngredients = Stream.of(catalog.getToppingList(), catalog.getFlavorList(), catalog.getDoughList()).flatMap(Collection::stream);
        assertTrue(allIngredients.anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(name)));
    }

    /**
     * We add a topping to the catalog through the facade
     **/
    @Given("^A new topping \"([^\"]*)\" is added to the catalog$")
    public void aNewToppingIsAddedToTheCatalog(String arg0) {
        context.getFacade().addTopping(arg0);
    }

    @When("^A new dough \"([^\"]*)\" is added to the catalog$")
    public void aNewDoughIsAddedToTheCatalog(String name) {
        context.getFacade().addDough(name);
    }

    @When("^A new flavor \"([^\"]*)\" is added to the catalog$")
    public void aNewFlavorIsAddedToTheCatalog(String name) {
        context.getFacade().addFlavor(name);
    }

    @Then("^The store \"([^\"]*)\" montly recipee contains ingredient \"([^\"]*)\"$")
    public void theStoreMontlyRecipeeContainsIngredient(String storeName, String ingredientName) {
        assertTrue(
                context.cookieFirm().findStore(storeName).orElseThrow(IllegalArgumentException::new)
                        .getMonthlyRecipe().getIngredients().stream().anyMatch(ingredient ->
                        ingredient.getName().equalsIgnoreCase(ingredientName)));
    }

}
