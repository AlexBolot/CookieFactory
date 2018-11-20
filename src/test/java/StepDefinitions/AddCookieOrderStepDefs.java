package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.*;
import main.*;
import order.Order;
import order.OrderLine;
import utils.CucumberContext;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utils.TestUtils.getInfiniteMockKitchen;

public class AddCookieOrderStepDefs {

    private final Map<String, Recipe> recipes = new HashMap<>();
    private final CookieFirm cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());
    private final CucumberContext context = CucumberContext.getContext();
    private final Guest guest = new Guest("guest");
    private Recipe currentRecipe;

    private Dough customdough;
    private Flavor customFlavor;
    private List<Topping> customToppings;
    private Mix customMix;
    private Cooking customCooking;

    @Given("^The guest see the list of cookies$")
    public void theGuestSeeTheListOfCookies() {
        this.guest.setTemporaryOrder(new Order());
        this.guest.getTemporaryOrder().setStore(new Store(null, Collections.emptyList(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1.0));
        this.guest.getTemporaryOrder().getStore().setKitchen(getInfiniteMockKitchen());
    }

    @When("^The guest select the recipee \"([^\"]*)\"$")
    public void theGuestSelectTheRecipee(String recipee) {
        final Catalog catalog = new Catalog();
        for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
            if (cookie.getName().equals(recipee)) {
                this.currentRecipe = cookie;
                return;
            }
        }

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
        guest.setTemporaryOrder(new Order());
        this.guest.getTemporaryOrder().setStore(new Store(null, Collections.emptyList(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1.0));
        this.guest.getTemporaryOrder().getStore().setKitchen(getInfiniteMockKitchen());
    }

    @And("^add (\\d+) cookie of the selected recipee$")
    public void addCookieOfTheSelectedRecipee(int cookieAmount) {
        this.guest.getTemporaryOrder().addCookie(currentRecipe, cookieAmount);
    }
    @And("^The guest choose the dough \"([^\"]*)\"$")
    public void theGuestChooseTheDough(String doughName) {
        this.customdough = context.utils.doughFromName(doughName);
    }

    @And("^The guest choose the flavor \"([^\"]*)\"$")
    public void theGuestChooseTheFlavor(String flavorName) {
        this.customFlavor = context.utils.flavorFromName(flavorName);
    }


    @And("^The guest choose the topping \"([^\"]*)\"$")
    public void theGuestChooseTheTopping(String toppingName) {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(context.utils.toppingFromName(toppingName));
        this.customToppings = toppings;
    }

    @And("^The guest choose the mix \"([^\"]*)\"$")
    public void theGuestChooseTheMix(String mixName) {
        this.customMix = context.utils.mixFromName(mixName);
    }

    @And("^The guest choose the cooking \"([^\"]*)\"$")
    public void theGuestChooseTheCooking(String cookingName) {
        this.customCooking = context.utils.cookingFromName(cookingName);
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

    @Then("^The order contain (\\d+) orderLines")
    public void theOrderContainCookie(int arg0) {
        assertEquals(arg0, this.guest.getTemporaryOrder().getOrderLines().size());
    }

    @And("^An order \"([^\"]*)\" at the store \"([^\"]*)\"$")
    public void anOrderAtTheStore(String orderName, String storeName) {
        Store store = context.getStore(storeName);
        Order order = context.getOrder(orderName);
        order.setStore(store);
    }

    @And("^The kitchen of \"([^\"]*)\" is empty$")
    public void theKitchenOfIsEmpty(String storeName) {
        context.getStore(storeName).setKitchen(new Kitchen());
    }

    @And("^The kitchen of \"([^\"]*)\" can do (\\d+) \"([^\"]*)\"$")
    public void theKitchenOfCanDo(String storeName, int recipeCount, String recipeName) {
        Store store = context.getStore(storeName);
        if (store.getKitchen() == null)
            store.setKitchen(new Kitchen());
        Kitchen kitchen = store.getKitchen();
        Recipe recipe = getRecipe(recipeName).orElse(null);
        if (recipe == null)
            return;
        kitchen.refill(recipe.getDough(), recipeCount);
        kitchen.refill(recipe.getFlavor(), recipeCount);
        recipe.getToppings().forEach(t -> kitchen.refill(t, recipeCount));
    }

    private Optional<Recipe> getRecipe(String recipeName) {
        return cookieFirm.getGlobalRecipes().stream().filter(r -> r.getName().equals(recipeName)).findFirst();
    }


    @And("^The guest is ordering the \"([^\"]*)\"$")
    public void theGuestIsOrderingThe(String orderName) {
        this.guest.setTemporaryOrder(context.getOrder(orderName));
    }

    @And("^The order contain (\\d+) cookie \"([^\"]*)\"$")
    public void theOrderContainCookieRecipe(int cookieCount, String recipee) {
        Recipe recipe = getRecipe(recipee).orElse(null);
        Optional<OrderLine> optionalOrderLine = guest.getTemporaryOrder()
                .getOrderLines().stream()
                .filter(line -> line.getRecipe().equals(recipe)).findFirst();
        assertTrue(optionalOrderLine.isPresent());
        assertEquals(optionalOrderLine.get().getAmount(), cookieCount);
    }
}
