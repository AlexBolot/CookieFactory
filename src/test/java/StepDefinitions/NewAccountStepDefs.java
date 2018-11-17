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
    private CookieFirm cookieFirm;

    private Mix mixFromName(String mixName) {
        for (Mix mix : catalog.getMixList()) {
            if (mix.getName().equalsIgnoreCase(mixName)) return mix;
        }
        return null;
    }
    private Cooking cookingFromName(String cookingName) {
        for (Cooking cooking : catalog.getCookingList()) {
            if (cooking.getName().equalsIgnoreCase(cookingName)) return cooking;
        }
        return null;
    }
    private Dough doughFromName(String doughName) {
        for (Dough dough : catalog.getDoughList()) {
            if (dough.getName().equalsIgnoreCase(doughName)) return dough;
        }
        return null;
    }
    private Topping toppingFromName(String toppingName) {
        for (Topping topping : catalog.getToppingList()) {
            if (topping.getName().equalsIgnoreCase(toppingName)) return topping;
        }
        return null;
    }
    private Flavor flavorFromName(String flavorName) {
        for (Flavor flavor : catalog.getFlavorList()) {
            if (flavor.getName().equalsIgnoreCase(flavorName)) return flavor;
        }
        return null;
    }

    @Given("^A recipe \"([^\"]*)\" as follows \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\" in sale at CookieFactory$")
    public void aRecipeAsFollowsInSaleInCookieFirm(String recipeName, String doughName, String flavorName,
                                                   String toppingName, String mixName, String cookingName) {
        List<Flavor> flavors = new ArrayList<>();
        flavors.add(flavorFromName(flavorName));
        List<Topping>toppings = new ArrayList<>();
        toppings.add(toppingFromName(toppingName));
        Recipe recipe = new Recipe(recipeName, doughFromName(doughName), flavors, toppings, mixFromName(mixName),cookingFromName(cookingName),0);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        cookieFirm = new CookieFirm(new ArrayList<Store>(), new ArrayList<Manager>(), recipes);
    }

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
