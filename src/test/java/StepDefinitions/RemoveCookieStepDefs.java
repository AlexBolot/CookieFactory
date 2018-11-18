package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ingredient.Catalog;
import main.Guest;
import main.Recipe;
import order.Order;

import static org.junit.Assert.assertEquals;

public class RemoveCookieStepDefs {
    private Guest guest;
    private Order order;

    @Given("^The customer checks his order$")
    public void theChecksHisOrder() throws Throwable {
        guest.setTemporaryOrder(order);
        order.setGuest(guest);
    }

    @When("^The customer select (\\d+) cookie to remove$")
    public void theSelectCookieToRemove(int cookieAmount) throws Throwable {
        order.removeCookie(order.getOrderLines().get(0).getRecipe(), cookieAmount);
    }


    @Given("^A customer$")
    public void aCustomer() throws Throwable {
        this.guest = new Guest("guest");
    }


    @Given("^An order with (\\d+) recipe of (\\d+) cookie$")
    public void anOrderWithRecipeOfCookie(int recipies, int cookies) throws Throwable {
        this.order = new Order();
        final Catalog kitchen = new Catalog();
        for (int i = 0; i < recipies; i++) {
            this.order.addCookie(
                    new Recipe(String.valueOf(i),
                            kitchen.getDoughList().get(0),
                            kitchen.getFlavorList().get(0),
                            kitchen.getToppingList().subList(0, 1),
                            kitchen.getMixList().get(0),
                            kitchen.getCookingList().get(0),
                            1.0f), cookies);
        }
    }

    @Then("^The order contains (\\d+) cookie recipee$")
    public void theOrderContainsCookieRecipee(int recipeeAmount) throws Throwable {
        assertEquals(recipeeAmount, this.order.getOrderLines().size());
    }
}
