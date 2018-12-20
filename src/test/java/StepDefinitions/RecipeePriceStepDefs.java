package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import store.Manager;
import utils.CucumberContext;

import java.util.Optional;

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

    @When("^\"([^\"]*)\" change the margin of custom cookies to (.+)$")
    public void changeTheMarginOfCustomCookies(String managerName, double margin) throws Throwable {
        context.getFacade().managerSetCustomCookieMargin(managerName, margin);
    }

    @Then("^\"([^\"]*)\" store have a margin for custom cookies of (.+)$")
    public void storeHaveAMarginForCustomCookies(String managerName, double margin) throws Throwable {
        context.cookieFirm().findManager(managerName).ifPresent(manager1 -> assertEquals(manager1.getStore().getCustomRecipeeMargin(), margin, 0));
    }
}
