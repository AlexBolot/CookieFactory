package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.Catalog;
import ingredient.Cooking;
import ingredient.Mix;
import main.Guest;
import main.Recipe;
import order.Order;

import static org.junit.Assert.assertEquals;

public class AddCookieOrderStepDefs {

    private Guest guest;
    private Recipe currentRecipe;

    @Given("^The guest see the list of cookies$")
    public void theGuestSeeTheListOfCookies() throws Throwable {
        this.guest = new Guest("guest");
        this.guest.setTemporaryOrder(new Order());
    }

    @When("^The guest select the recipee \"([^\"]*)\"$")
    public void theGuestSelectTheRecipee(String recipee) throws Throwable {
        final Catalog catalog = new Catalog();
        this.currentRecipe = new Recipe(recipee, catalog.getDoughList().get(0), catalog.getFlavorList().subList(0, 1), catalog.getToppingList().subList(0, 1), Mix.MIXED, Cooking.CHEWY, 1f);

    }

    @And("^add (\\d+) cookie of the selected recipee$")
    public void addCookieOfTheSelectedRecipee(int cookieAmount) throws Throwable {
        this.guest.getTemporaryOrder().addCookie(currentRecipe, cookieAmount);
    }

    @Then("^The order contain \"([^\"]*)\" cookie$")
    public void theOrderContainCookie(String arg0) throws Throwable {
        assertEquals(arg0, this.guest.getTemporaryOrder().getOrderLines().size());
    }
}
