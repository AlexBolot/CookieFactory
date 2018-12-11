package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import utils.CucumberContext;

public class OrderCustomCookieStepdefs {
    private final CucumberContext context = CucumberContext.getContext();

    @Given("^The guest order (\\d+) cookies of the custom recipe with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void theGuestOrderCookiesOfTheCustomRecipeWithAnd(int quantity, String dough, String flavor,
                                                             String topping, String topping2, String topping3,
    String mix, String cookin) throws Throwable {
        context.getFacade().guestOrderCustomCookie(context.getCurrentId(), dough, flavor, topping, topping2,
                topping3,
                mix,
                cookin,
                quantity);
    }
}
