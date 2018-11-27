package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import order.Order;
import store.Store;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.getInfiniteMockKitchen;

public class CustomerOrderStepDefs {

    private final CucumberContext context= CucumberContext.getContext();

    private DayOfWeek dayFromName(String dayName) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }

    @Given("^An order \"([^\"]*)\" at the store \"([^\"]*)\", to pickup \"([^\"]*)\" (\\d+) hour before closing time$")
    public void anOrderAtTheStoreToPickupHourBeforeClosingTime(String orderName, String storeName, String dayName, int hoursBeforeEnd) {
        Store store = context.stores.get(storeName);
        store.setKitchen(getInfiniteMockKitchen());
        LocalDateTime pickTime =LocalDateTime.of(LocalDate.now(), store.closingTime(dayFromName(dayName)).minusHours(hoursBeforeEnd))
                .with(TemporalAdjusters.next(DayOfWeek.valueOf(dayName.toUpperCase())));
        Order order = new Order(store, pickTime);
        order.addCookie(context.utils.randomRecipe(), 5);

        context.orders.put(orderName, order);

    }


    @When("^\"([^\"]*)\" validates the order \"([^\"]*)\"$")
    public void validatesTheOrder(String name, String orderName) {

        Customer customer = context.getCustomer(name);
        Order order = context.orders.get(orderName);

       // customer.setTemporaryOrder(order);
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

    @Given("^The store \"([^\"]*)\" opens \"([^\"]*)\" (\\d+) hours ago and closes in (\\d+) hours$")
    public void theStoreOpensHoursAgoAndClosesInHours(String storeName, String dayName, int behindHours, int aheadHours) {

        DayOfWeek day = context.utils.dayFromName(dayName);

        LocalTime opTime = LocalTime.now().minusHours(behindHours);
        LocalTime clTime = LocalTime.now().plusHours(aheadHours);

        Store store = context.stores.get(storeName);

        store.setOpeningTime(day, opTime);
        store.setClosingTime(day, clTime);
    }
}


