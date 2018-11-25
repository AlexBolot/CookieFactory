package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.CookieFirm;
import main.Customer;
import main.Guest;
import order.Order;
import org.junit.Assert;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Store;
import utils.CucumberContext;

import java.util.ArrayList;
import java.util.HashMap;

import static utils.TestUtils.getInfiniteMockKitchen;

public class NewAccountStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private final Catalog catalog = new Catalog();
    private final CookieFirm cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());

    @Given("^An order \"([^\"]*)\" with (\\d+) cookies \"([^\"]*)\"$")
    public void anOrderWithCookiesAsFollows(String orderName, int nbrCookies, String recipeName) {
        Order order = new Order(new Store("", null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(),1)
                , null, null);
        order.getStore().setKitchen(getInfiniteMockKitchen());
        for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
            if (cookie.getName().equals(recipeName)){
                order.addCookie(cookie, nbrCookies);
                context.orders.put(orderName, order);
                return;
            }
        }
    }

    @And("^A guest \"([^\"]*)\" is ordering the order \"([^\"]*)\"$")
    public void aGuestOrderingTheOrder(String guestName, String orderName) {
        context.getGuest(guestName).setTemporaryOrder(context.orders.get(orderName));
    }

    @And("^The guest \"([^\"]*)\" gives \"([^\"]*)\" as email$")
    public void theGuestGivesAsEmail(String guestName, String email) {
        context.getGuest(guestName).setEmail(email);
    }

    @When("^The guest \"([^\"]*)\" create an account \"([^\"]*)\" at the name of \"([^\"]*)\" \"([^\"]*)\" with the password \"([^\"]*)\" and the phone \"([^\"]*)\"$")
    public void theGuestCreateAnAccountATheNameOfWithThePassword(String guestName, String accountName, String firstName, String lastName, String password, String phoneNumber) {
        Guest guest = context.getGuest(guestName);
        Customer account = cookieFirm.createAccount(firstName, lastName, phoneNumber, guest.getEmail(), password, guest.getTemporaryOrder());
        context.addGuest(accountName, account);
    }

    @Then("^The account \"([^\"]*)\" is saved$")
    public void anAccountIsCreatedWithTheMailAndThePassword(String accountName) {
        Assert.assertTrue(cookieFirm.getAccounts().contains(context.getCustomer(accountName)));
    }

    @And("^The order \"([^\"]*)\" is saved in the account \"([^\"]*)\"$")
    public void theOrderOfIsSaved(String orderName, String accountName) {
        Assert.assertEquals(context.getCustomer(accountName).getTemporaryOrder(), context.orders.get(orderName));
    }
}
