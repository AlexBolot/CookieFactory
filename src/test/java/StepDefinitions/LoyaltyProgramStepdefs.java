package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import main.CookieFirm;
import main.Customer;
import order.Order;
import recipe.Recipe;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class LoyaltyProgramStepdefs {

    private final CucumberContext context = CucumberContext.getContext();

    private double price;
    private Recipe recipe;

    @Given("^\"([^\"]*)\" is in the loyaltyProgram$")
    public void isInTheLoyaltyProgram(String sCustomer) {
        Customer frank = context.getCustomer(sCustomer);
        CookieFirm cookieFirm = context.getCookieFirm();
        cookieFirm.addCustomerToLoyaltyProgram(frank);
    }

    @Given("^\"([^\"]*)\" made an \"([^\"]*)\" into the \"([^\"]*)\" in (\\d+) hours, on \"([^\"]*)\", with (\\d+) cookies$")
    public void madeAnIntoTheInHoursOnWithCookies(String sCustomer, String sOrder, String sStore, int time, String day, int numberCookies) {

        Order order1 = new Order(context.stores.get(sStore), LocalDateTime.now().plusHours(time).with(TemporalAdjusters.next(DayOfWeek.valueOf(day.toUpperCase()))));

        recipe = context.utils.randomRecipe();
        order1.addCookie(recipe, numberCookies);


        Customer cus = context.getCustomer(sCustomer);
        cus.setTemporaryOrder(order1);
        context.orders.put(sOrder, order1);
        price = cus.placeOrder(true);
    }

    @When("^\"([^\"]*)\" see the price of his \"([^\"]*)\" with (\\d+) cookies it have a discount$")
    public void seeThePriceOfHisWithCookiesItHaveADiscountOfPourcent(String sCustomer, String sOrder, int numberCookie) {
        Order order = context.orders.get(sOrder);
        //TODO uncoment soon
        // Assert.assertEquals(recipe.price * numberCookie * 0.9, price, 0.001);
    }
}
