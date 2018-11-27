package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Guest;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.getInfiniteMockKitchen;

public class RemoveCookieStepDefs {

    private Guest guest;
    private Order order;

    @Given("^The customer checks his order$")
    public void theChecksHisOrder() {
        order = guest.getTemporaryOrder();
        order.setGuest(guest);
    }

    @When("^The customer select (\\d+) cookie to remove$")
    public void theSelectCookieToRemove(int cookieAmount) {
        order.removeCookie(order.getOrderLines().get(0).getRecipe(), cookieAmount);
    }

    @Given("^A guest$")
    public void aGuest() {
        this.guest = new Guest();
    }

    @Given("^An order with (\\d+) recipe of (\\d+) cookie$")
    public void anOrderWithRecipeOfCookie(int recipies, int cookies) {
        this.order = new Order();
        this.order.setStore(new Store("",null, Collections.emptyList(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1.0));
        this.order.getStore().setKitchen(getInfiniteMockKitchen());
        final Catalog kitchen = new Catalog();
        for (int i = 0; i < recipies; i++) {
            Recipe recipe = new Recipe(String.valueOf(i),
                    kitchen.getDoughList().get(0),
                    kitchen.getFlavorList().get(0),
                    kitchen.getToppingList().subList(0, 1),
                    kitchen.getMixList().get(0),
                    kitchen.getCookingList().get(0),
                    true);
            this.order.addCookie(
                    recipe, cookies);
        }
    }

    @Then("^The order contains (\\d+) cookie recipee$")
    public void theOrderContainsCookieRecipee(int recipeeAmount) {
        assertEquals(recipeeAmount, this.order.getOrderLines().size());
    }
}
