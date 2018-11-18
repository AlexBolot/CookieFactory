package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.*;
import main.*;
import order.Order;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAccountStepDefs {

    private Map<String, Order> orders = new HashMap<>();
    private Map<String, Guest> guests = new HashMap<>();
    private Map<String, Customer> accounts = new HashMap<>();
    private Catalog catalog = new Catalog();
    private CookieFirm cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());

    @Given("^An order \"([^\"]*)\" with (\\d+) cookies \"([^\"]*)\"$")
    public void anOrderWithCookiesAsFollows(String orderName, int nbrCookies, String recipeName) {
        Order order = new Order(new Store(null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(),1)
                , null, null);
        for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
            if (cookie.getName().equals(recipeName)){
                order.addCookie(cookie, nbrCookies);
                orders.put(orderName, order);
                return;
            }
        }
    }

    @And("^A guest \"([^\"]*)\" is ordering the order \"([^\"]*)\"$")
    public void aGuestOrderingTheOrder(String guestName, String orderName) {
        guests.get(guestName).setTemporaryOrder(orders.get(orderName));
    }

    @And("^The guest \"([^\"]*)\" gives \"([^\"]*)\" as email$")
    public void theGuestGivesAsEmail(String guestName, String email) {
        guests.get(guestName).setEmail(email);
    }

    @When("^The guest \"([^\"]*)\" create an account \"([^\"]*)\" at the name of \"([^\"]*)\" \"([^\"]*)\" with the password \"([^\"]*)\" and the phone \"([^\"]*)\"$")
    public void theGuestCreateAnAccountATheNameOfWithThePassword(String guestName,String accountName, String firstName, String lastName, String password, String phoneNumber) {
        Customer account = cookieFirm.createAccount(firstName, lastName,phoneNumber,guests.get(guestName).getEmail(),password, guests.get(guestName).getTemporaryOrder());
        accounts.put(accountName, account);
    }

    @Then("^The account \"([^\"]*)\" is saved$")
    public void anAccountIsCreatedWithTheMailAndThePassword(String accountName) {
        Assert.assertTrue(cookieFirm.getAccounts().contains(accounts.get(accountName)));
    }

    @And("^The order \"([^\"]*)\" is saved in the account \"([^\"]*)\"$")
    public void theOrderOfIsSaved(String orderName, String accountName) {
        Assert.assertTrue(accounts.get(accountName).getTemporaryOrder().equals(orders.get(orderName)));
    }

    @Given("^A guest \"([^\"]*)\"$")
    public void aGuest(String guestName) {
        guests.put(guestName, new Guest(""));
    }
}
