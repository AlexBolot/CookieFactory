package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import main.Day;
import order.Order;
import utils.CucumberContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class CustomerOrderStepDefs {

    private final CucumberContext context= CucumberContext.getContext();

    private Day dayFromName(String dayName) {
        for (Day day : Day.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }

    @Given("^An order \"([^\"]*)\" at the store \"([^\"]*)\", to pickup \"([^\"]*)\" at \"([^\"]*)\"$")
    public void anOrderAtTheStoreToPickupAt(String orderName, String storeName, String dayName, String pickupTime) {
        int pickHours = Integer.parseInt(pickupTime.split(":")[0]);
        int pickMinutes = Integer.parseInt(pickupTime.split(":")[1]);

        LocalDateTime pickTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(pickHours, pickMinutes));

        Order order = new Order(context.stores.get(storeName), pickTime, dayFromName(dayName));
        order.addCookie(context.utils.randomRecipe(), 5);

        context.orders.put(orderName, order);
    }

    @When("^\"([^\"]*)\" validates the order \"([^\"]*)\"$")
    public void validatesTheOrder(String name, String orderName) {

        Customer customer = context.getCustomer(name);
        Order order = context.orders.get(orderName);

        customer.setTemporaryOrder(order);
        customer.placeOrder(true);
    }


    @Then("^\"([^\"]*)\" has (\\d+) order in his history$")
    public void hasOrderInHisHistory(String name, int amount) {
        Customer customer = context.getCustomer(name);

        assertEquals(amount, customer.getOrderHistory().size());
    }

    @And("^\"([^\"]*)\" has an empty temporary order$")
    public void hasAnEmptyTempraryOrder(String name) {
        Customer customer = context.getCustomer(name);
        assertEquals(new Order(), customer.getTemporaryOrder());
    }
}


