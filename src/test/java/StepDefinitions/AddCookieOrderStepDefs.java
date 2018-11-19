package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.*;
import main.CookieFirm;
import main.Guest;
import main.Recipe;
import order.Order;
import order.OrderLine;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AddCookieOrderStepDefs {

    private Guest guest;
    private final Map<String, Recipe> recipes = new HashMap<>();
    private final CookieFirm cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());

    private Dough customdough;
    private Flavor customFlavor;
    private List<Topping> customToppings;
    private Mix customMix;
    private Cooking customCooking;

    private TestUtils utils = new TestUtils();

    @Given("^The guest see the list of cookies$")
    public void theGuestSeeTheListOfCookies() {
        this.guest = new Guest("guest");
        this.guest.setTemporaryOrder(new Order());
    }

    @When("^The guest order (\\d+) cookie \"([^\"]*)\"$")
    public void theGuestOrderCookie(int amount, String recipeName) {
        for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
            if (cookie.getName().equals(recipeName)) {
                recipes.put(recipeName, cookie);
                guest.getTemporaryOrder().addCookie(cookie, amount);
                return;
            }
        }
    }

    @Then("^The order contains (\\d+) cookie \"([^\"]*)\"$")
    public void theOrderContainCookie(int amount, String recipeName) {
        for (OrderLine line : guest.getTemporaryOrder().getOrderLines()) {
            if (recipes.get(recipeName).equals(line.getRecipe())) {
                assertEquals(amount, line.getAmount());
            }
        }
    }

    @Given("^The guest see the list of ingredients$")
    public void theGuestSeeTheListOfIngredients() {
        guest = new Guest("guest");
        guest.setTemporaryOrder(new Order());
    }

    @And("^The guest choose the dough \"([^\"]*)\"$")
    public void theGuestChooseTheDough(String doughName) {
        this.customdough = utils.doughFromName(doughName);
    }

    @And("^The guest choose the flavor \"([^\"]*)\"$")
    public void theGuestChooseTheFlavor(String flavorName) {
        this.customFlavor = utils.flavorFromName(flavorName);
    }

    @And("^The guest choose the topping \"([^\"]*)\"$")
    public void theGuestChooseTheTopping(String toppingName) {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(utils.toppingFromName(toppingName));
        this.customToppings = toppings;

    }

    @And("^The guest choose the mix \"([^\"]*)\"$")
    public void theGuestChooseTheMix(String mixName) {
        this.customMix = utils.mixFromName(mixName);
    }

    @And("^The guest choose the cooking \"([^\"]*)\"$")
    public void theGuestChooseTheCooking(String cookingName) {
        this.customCooking = utils.cookingFromName(cookingName);
    }

    @When("^The guest order (\\d+) custom cookie \"([^\"]*)\"$")
    public void theGuestOrderCustomCookie(int amount, String recipeName) {
        recipes.put(recipeName, guest.orderCustomRecipe(amount,
                this.customdough,
                this.customFlavor,
                this.customToppings,
                this.customMix,
                this.customCooking));
    }
}
