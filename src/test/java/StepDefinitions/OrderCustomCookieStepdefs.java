package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import utils.CucumberContext;

public class OrderCustomCookieStepdefs {
    private final CucumberContext context = CucumberContext.getContext();

    private Exception wrongRecipeException;

    @Given("^The guest order (\\d+) cookies of the custom recipe with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void theGuestOrderCookiesOfTheCustomRecipeWithAnd(int quantity,
                                                             String dough,
                                                             String flavor,
                                                             String topping,
                                                             String topping2,
                                                             String topping3,
                                                             String mix,
                                                             String cooki) {
        context.getFacade().guestOrderCustomCookie(context.getCurrentId(),
                dough,
                flavor,
                topping,
                topping2,
                topping3,
                mix,
                cooki,
                quantity);
    }

    @When("^The guest try to order (\\d+) cookies of the wrong custom recipe with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void theGuestTryToOrderCookiesOfTheWrongCustomRecipeWithAnd(int amount,
                                                                       String dough,
                                                                       String flavor,
                                                                       String topping1,
                                                                       String topping2,
                                                                       String topping3,
                                                                       String mix,
                                                                       String cooking) {
        try {
            context.getFacade().guestOrderCustomCookie(context.getCurrentId(),
                    dough,
                    flavor,
                    topping1,
                    topping2,
                    topping3,
                    mix,
                    cooking,
                    amount);
        } catch (Exception e) {
            wrongRecipeException = e;
        }
    }

    @Then("^The custom recipe ordering fails$")
    public void theCustomRecipeOrderingFails() {
        Exception exception = new IllegalArgumentException("This dough does not exist in the catalog");
        Assert.assertEquals(exception.getMessage(), wrongRecipeException.getMessage());
        wrongRecipeException = null;
    }
}
