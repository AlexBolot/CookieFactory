package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import order.Order;
import store.Store;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.getInfiniteMockKitchen;

public class CustomerOrderStepDefs {

    private final CucumberContext context= CucumberContext.getContext();
    private TestUtils utils = new TestUtils();


    @Then("^\"([^\"]*)\" has (\\d+) order in his history$")
    public void hasOrderInHisHistory(String name, int amount) {
        Optional<Customer> opCustomer = context.cookieFirm().findCustomer(utils.createEmail(name));
        if(opCustomer.isPresent())
            assertEquals(amount, opCustomer.get().getOrderHistory().size());
    }

    @And("^\"([^\"]*)\" has an empty temporary order$")
    public void hasAnEmptyTempraryOrder(String name) {
        Optional<Customer> opCustomer = context.cookieFirm().findCustomer(utils.createEmail(name));
        if(opCustomer.isPresent())
            assertEquals(new Order(), opCustomer.get().getTemporaryOrder());
    }



}


