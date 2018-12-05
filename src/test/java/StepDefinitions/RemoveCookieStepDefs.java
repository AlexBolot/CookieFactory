package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Guest;
import order.Order;
import order.OrderLine;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Store;
import utils.CucumberContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.getInfiniteMockKitchen;

public class RemoveCookieStepDefs {

    private final CucumberContext context= CucumberContext.getContext();


    @When("^The customer select (\\d+) cookie to remove from the \"([^\"]*)\"$")
    public void theSelectCookieToRemove(int cookieAmount, String store) {
        context.getFacade().guestRemoveCookies(context.getCurrentId(), store, cookieAmount);
    }


    @Then("^The order contains (\\d+) cookie recipee$")
    public void theOrderContainsCookieRecipee(int recipeeAmount) {
        Optional<Guest> guest = context.cookieFirm().findGuest(context.getCurrentId());
        if(guest.isPresent()){
            int count = 0;
            for(OrderLine orderLine : guest.get().getTemporaryOrder().getOrderLines()) {
                count += orderLine.getAmount();
            }
            assertEquals(count, recipeeAmount);
        }
    }
}
