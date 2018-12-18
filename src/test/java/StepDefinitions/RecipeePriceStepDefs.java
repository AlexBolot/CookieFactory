package StepDefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import utils.CucumberContext;

import static org.junit.Assert.assertEquals;

public class RecipeePriceStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @When("^\"([^\"]*)\" checks the price of \"([^\"]*)\"$")
    public void checksThePriceOf(String managerName, String recipee) throws Throwable {
        context.getFacade().managerQueryPriceOf(managerName, recipee);
    }

    @Then("^The price of \"([^\"]*)\" for \"([^\"]*)\" is (.+)$")
    public void thePriceOfForIs(String recipeeName, String managerName, double price) throws Throwable {
        assertEquals(price, context.getFacade().managerQueryPriceOf(managerName, recipeeName), 0.001);
    }
}
