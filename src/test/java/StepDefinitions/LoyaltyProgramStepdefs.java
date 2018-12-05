package StepDefinitions;

import cucumber.api.PendingException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LoyaltyProgramStepdefs {

    private final CucumberContext context = CucumberContext.getContext();


    @Given("^\"([^\"]*)\" is in the loyaltyProgram$")
    public void isInTheLoyaltyProgram(String sCustomer) {
        context.getFacade().addACustomerToLP(sCustomer);
    }

    @When("^\"([^\"]*)\" see the price of his order with (\\d+) cookies it have a discount$")
    public void seeThePriceOfHisOrderWithCookiesItHaveADiscount(String sCustomer, int nbCookie) throws Throwable {
        Optional<Customer> customer = context.cookieFirm().findCustomer(sCustomer);
        double price;
        if(customer.isPresent()) {
            Collection<Order> order = customer.get().getOrderHistory();
            List<Order> orderList = new ArrayList<>(order);
            Order currentOrder = orderList.get(orderList.size()-1);

            double orderLinePrices = currentOrder.getOrderLines().stream().mapToDouble(line -> line.getAmount() * currentOrder.getStore().getRecipePrice(line.getRecipe())).sum();

            if(currentOrder.didItHadDiscount()){
                price = currentOrder.getPrice() * 0.9;
            }
            else
            {
                price = currentOrder.getPrice();
            }
            assertEquals(orderLinePrices * 0.9, price, 0);
        }
    }
}
