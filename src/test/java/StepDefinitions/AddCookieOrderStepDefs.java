package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Guest;
import order.OrderLine;
import recipe.Recipe;
import store.Kitchen;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class AddCookieOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();
    private Recipe currentRecipe;

    @Given("^The guest see the list of cookies$")
    public void theGuestSeeTheListOfCookies() {
    }

    @When("^The guest select the recipee \"([^\"]*)\" of the \"([^\"]*)\"$")
    public void theGuestSelectTheRecipee(String recipee, String sStore) {
        for (Recipe cookie : context.cookieFirm().getGlobalRecipes()) {
            if (cookie.getName().equals(recipee)) {
                this.currentRecipe = cookie;
                return;
            }
        }

    }

    @When("^The guest order (\\d+) cookie \"([^\"]*)\" from the \"([^\"]*)\"$")
    public void theGuestOrderCookie(int amount, String recipeName, String store) {
        context.getFacade().guestAddOrRemoveCookie(context.getCurrentId(), store, amount, recipeName, false);
    }

    @Then("^The order contains (\\d+) cookie \"([^\"]*)\"$")
    public void theOrderContainCookie(int amount, String recipeName) {
        Optional<Guest> guest = context.cookieFirm().findGuest(context.getCurrentId());

        if (guest.isPresent()) {
            for (OrderLine line : guest.get().getTemporaryOrder().getOrderLines()) {
                if (line.getRecipe().getName().equals(recipeName)) {
                    assertEquals(amount, line.getAmount());
                }
            }
        }
    }

    @Then("^The order contain (\\d+) orderLines$")
    public void theOrderContainOrderLines(int amount) {

        Optional<Guest> guest = context.cookieFirm().findGuest(context.getCurrentId());

        guest.ifPresent(guest1 -> assertEquals(amount, guest1.getTemporaryOrder().getOrderLines().size()));
    }


    @And("^The kitchen of \"([^\"]*)\" is empty$")
    public void theKitchenOfIsEmpty(String storeName) {
        Optional<Store> store = context.cookieFirm().findStore(storeName);
        store.ifPresent(store1 -> store1.setKitchen(new Kitchen()));
    }

    @And("^add (\\d+) cookie of the selected recipee in the \"([^\"]*)\"$")
    public void addCookieOfTheSelectedRecipee(int cookieAmount, String store) {
        context.getFacade().guestAddOrRemoveCookie(context.getCurrentId(), store, cookieAmount, currentRecipe.getName(), false);
    }

}
