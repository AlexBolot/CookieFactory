package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.CookieFirm;
import main.Guest;
import order.Order;
import order.OrderLine;
import org.junit.Assert;
import recipe.Recipe;
import recipe.ingredient.*;
import store.Kitchen;
import store.Store;
import utils.CucumberContext;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utils.TestUtils.getInfiniteMockKitchen;

public class AddCookieOrderStepDefs {

    private final Map<String, Recipe> recipes = new HashMap<>();
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
        context.getFacade().guestAddSpecificCookie(context.getCurrentId(),store,amount, recipeName);
    }

    @Then("^The order contains (\\d+) cookie \"([^\"]*)\"$")
    public void theOrderContainCookie(int amount, String recipeName) {
        Optional<Guest> guest = context.cookieFirm().findGuest(context.getCurrentId());

        if(guest.isPresent()) {
            for (OrderLine line : guest.get().getTemporaryOrder().getOrderLines()){
                if (recipes.get(recipeName).equals(line.getRecipe())) {
                    assertEquals(amount, line.getAmount());
                }
            }
        }
    }

    @And("^The kitchen of \"([^\"]*)\" is empty$")
    public void theKitchenOfIsEmpty(String storeName) {
        Optional<Store> store = context.cookieFirm().findStore(storeName);
        store.get().setKitchen(new Kitchen());
    }

    @And("^The guest is ordering at the store \"([^\"]*)\"$")
    public void theGuestIsOrderingAtTheStore(String sStore) throws Throwable {
        context.getFacade().guestAddStoreToOrder(context.getCurrentId(), sStore);
    }

    @Given("^The guest see the list of ingredients$")
    public void theGuestSeeTheListOfIngredients() {
        /*this.guest.getTemporaryOrder().setStore(new Store("", null, Collections.emptyList(), new
                HashMap<>(), new HashMap<>(), 1.0, 1));
        this.guest.getTemporaryOrder().getStore().setKitchen(getInfiniteMockKitchen());*/
    }

    @And("^add (\\d+) cookie of the selected recipee in the \"([^\"]*)\"$")
    public void addCookieOfTheSelectedRecipee(int cookieAmount, String store) {
        context.getFacade().guestAddSpecificCookie(context.getCurrentId(),store, cookieAmount, currentRecipe.getName());
    }

    @When("^The guest order (\\d+) custom cookie \"([^\"]*)\"$")
    public void theGuestOrderCustomCookie(int amount, String recipeName) {
       /* recipes.put(recipeName, guest.orderCustomRecipe(amount,
                this.customDough,
                this.customFlavor,
                this.customToppings,
                this.customMix,
                this.customCooking));*/
    }

    @Then("^The order contain (\\d+) orderLines")
    public void theOrderContainCookie(int arg0) {
       // assertEquals(arg0, this.guest.getTemporaryOrder().getOrderLines().size());
    }





    @And("^The kitchen of \"([^\"]*)\" can do (\\d+) \"([^\"]*)\"$")
    public void theKitchenOfCanDo(String storeName, int recipeCount, String recipeName) {

        Store store = context.getStore(storeName);
        Kitchen kitchen = new Kitchen();
        Recipe recipe = getRecipe(recipeName).orElse(null);
        if (recipe == null)
            return;
        kitchen.refill(recipe.getDough(), recipeCount);
        kitchen.refill(recipe.getFlavor(), recipeCount);
        recipe.getToppings().forEach(t -> kitchen.refill(t, recipeCount));

        store.setKitchen(kitchen);
    }

    private Optional<Recipe> getRecipe(String recipeName) {
        return context.cookieFirm().getGlobalRecipes().stream().filter(r -> r.getName().equals(recipeName)).findFirst();
    }


    @And("^The guest is ordering the \"([^\"]*)\"$")
    public void theGuestIsOrderingThe(String orderName) {
      //  this.guest.setTemporaryOrder(context.getOrder(orderName));
    }

    @And("^The order contain (\\d+) cookie \"([^\"]*)\"$")
    public void theOrderContainCookieRecipe(int cookieCount, String recipee) {
       /* Recipe recipe = getRecipe(recipee).orElse(null);
        Optional<OrderLine> optionalOrderLine =
                guest.getTemporaryOrder()
                        .getOrderLines().stream()
                        .filter(line -> line.getRecipe().equals(recipe)).findFirst();
        assertTrue(optionalOrderLine.isPresent());
        assertEquals(optionalOrderLine.get().getAmount(), cookieCount);*/
    }

    @When("^The manager refill the stock of \"([^\"]*)\" \"([^\"]*)\" by (\\d+) in the kitchen of \"([^\"]*)\"$")
    public void theManagerRefillTheStockOfToppingByInTheKitchenOf(String ingredientType, String ingredientName, int amount, String storeName) {
        switch (ingredientType) {
            case "topping":
                context.getStore(storeName).getKitchen().refill(context.utils.toppingFromName(ingredientName),amount);
                break;
            case "flavor":
                context.getStore(storeName).getKitchen().refill(context.utils.toppingFromName(ingredientName),amount);
                break;
            case "dough":
                context.getStore(storeName).getKitchen().refill(context.utils.toppingFromName(ingredientName),amount);
                break;
            default:
                //Do nothing
        }
    }

    @Then("^The kitchen of \"([^\"]*)\" has exactly (\\d+) \"([^\"]*)\" \"([^\"]*)\"$")
    public void theKitchenOfContainsTopping(String storeName, int amount,String ingredientType, String ingredientName) {
        switch (ingredientType) {
            case "topping":
                Assert.assertTrue(context.getStore(storeName).getKitchen().hasInStock(context.utils.toppingFromName(ingredientName),amount));
                Assert.assertFalse(context.getStore(storeName).getKitchen().hasInStock(context.utils.toppingFromName(ingredientName),amount+1));
                break;
            case "flavor":
                Assert.assertTrue(context.getStore(storeName).getKitchen().hasInStock(context.utils.flavorFromName(ingredientName),amount));
                Assert.assertFalse(context.getStore(storeName).getKitchen().hasInStock(context.utils.flavorFromName(ingredientName),amount+1));
                break;
            case "dough":
                Assert.assertTrue(context.getStore(storeName).getKitchen().hasInStock(context.utils.doughFromName(ingredientName),amount));
                Assert.assertFalse(context.getStore(storeName).getKitchen().hasInStock(context.utils.doughFromName(ingredientName),amount+1));
                break;
            default:
                //Do nothing
        }
    }


}
