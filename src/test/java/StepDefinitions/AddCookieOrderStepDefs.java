package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.Catalog;
import main.CookieFirm;
import main.Guest;
import main.Recipe;
import order.Order;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AddCookieOrderStepDefs {

    private Guest guest;
    private Recipe currentRecipe;
    private final CookieFirm cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());

    @Given("^The guest see the list of cookies$")
    public void theGuestSeeTheListOfCookies() {
        this.guest = new Guest("guest");
        this.guest.setTemporaryOrder(new Order());
    }

    @When("^The guest select the recipee \"([^\"]*)\"$")
    public void theGuestSelectTheRecipee(String recipee) {
        final Catalog catalog = new Catalog();
        for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
            if (cookie.getName().equals(recipee)){
                this.currentRecipe=cookie;
                return;
            }
        }

    }

    @And("^add (\\d+) cookie of the selected recipee$")
    public void addCookieOfTheSelectedRecipee(int cookieAmount) {
        this.guest.getTemporaryOrder().addCookie(currentRecipe, cookieAmount);
    }

    @Then("^The order contain (\\d+) cookie$")
    public void theOrderContainCookie(int arg0) {
        assertEquals(arg0, this.guest.getTemporaryOrder().getOrderLines().size());
    }
}
