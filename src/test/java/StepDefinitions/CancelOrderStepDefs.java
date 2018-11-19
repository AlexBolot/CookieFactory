package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.*;
import order.Order;
import order.OrderState;
import org.junit.Assert;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CancelOrderStepDefs {

    private TestUtils utils = new TestUtils();

    private final CucumberContext context= CucumberContext.getContext();

    private Collection<Order> ordersStore = new ArrayList<>();
    private Order currentOrder;


    @Given("^\"([^\"]*)\" made an \"([^\"]*)\" into the \"([^\"]*)\" in (\\d+) hours, on \"([^\"]*)\"$")
    public void madeAnIntoTheInHoursOn(String sCustomer, String sOrder, String sStore, int iTime, String sDay) throws Throwable {
        Order order1 = new Order(context.stores.get(sStore),LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (iTime))), context.utils.dayFromName(sDay) );
        order1.addCookie(context.utils.randomRecipe(),10);
        Customer customer1 = new Customer("Jack","Jack","","jack@jack.com","", order1);
        context.getCustomer(sCustomer);
        context.orders.put(sOrder, order1);
        customer1.placeOrder(true);
    }

    @And("^An employee see the \"([^\"]*)\"'s orders$")
    public void anEmployeeSeeTheSOrders(String sStore) throws Throwable {
        ordersStore =  context.stores.get(sStore).getOrders();
       // System.out.println(ordersStore.size());
    }

    @When("^An employee of \"([^\"]*)\" cancel the \"([^\"]*)\"$")
    public void anEmployeeOfCancelThe(String sStore, String sNameOrder) throws Throwable {
        Order targetOrder = context.orders.get(sNameOrder);
        currentOrder = context.stores.get(sStore)
                .findOrder(targetOrder.getPickUpTime(), targetOrder.getPickupDay(), targetOrder.getGuest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Not found the order"));
        currentOrder.cancel();
    }

    @Then("^The order is no longer in the list of orders of the \"([^\"]*)\"$")
    public void theOrderIsNoLongerInTheListOfOrdersOfThe(String sStore) throws Throwable {
      //  Assert.assertTrue(ordersStore.size()-1 == context.stores.get(sStore).getOrders().size());

    }
    @Then("^\"([^\"]*)\" is \"([^\"]*)\"$")
    public void is(String sOrder, String sEtat) throws Throwable {
        Assert.assertEquals(context.orders.get(sOrder).getState(), context.utils.stateFromName(sEtat));
    }

    @And("^\"([^\"]*)\" receveid a \"([^\"]*)\"$")
    public void receveidA(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }



}
