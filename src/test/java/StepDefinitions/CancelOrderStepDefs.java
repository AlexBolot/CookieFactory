package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import order.Order;
import org.junit.Assert;
import utils.CucumberContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CancelOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Collection<Order> ordersStore = new ArrayList<>();
    private Order currentOrder;


    @Given("^\"([^\"]*)\" made an \"([^\"]*)\" into the \"([^\"]*)\" in (\\d+) hours, on \"([^\"]*)\"$")
    public void madeAnIntoTheInHoursOn(String sCustomer, String sOrder, String sStore, int iTime, String sDay) {
        Order order1 = new Order(context.stores.get(sStore), LocalDateTime.now().plusHours(iTime));
        order1.addCookie(context.utils.randomRecipe(), 10);


        Customer jack = context.getCustomer(sCustomer);
        context.orders.put(sOrder, order1);
        jack.setTemporaryOrder(order1);
        jack.placeOrder(true);

    }

    @And("^An employee see the \"([^\"]*)\"'s orders$")
    public void anEmployeeSeeTheSOrders(String sStore) {
        ordersStore = context.stores.get(sStore).getOrders();
        // System.out.println(ordersStore.size());
    }

    @When("^An employee of \"([^\"]*)\" cancel the \"([^\"]*)\"$")
    public void anEmployeeOfCancelThe(String sStore, String sNameOrder) {
        Order targetOrder = context.orders.get(sNameOrder);
        currentOrder = context.stores.get(sStore)
                .findOrder(targetOrder.getPickUpTime(), targetOrder.getGuest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Not found the order"));
        currentOrder.cancel();
    }

    @Then("^The order is no longer in the list of orders of the \"([^\"]*)\"$")
    public void theOrderIsNoLongerInTheListOfOrdersOfThe(String sStore) {
        //  Assert.assertTrue(ordersStore.size()-1 == context.stores.get(sStore).getOrders().size());

    }

    @Then("^\"([^\"]*)\" is \"([^\"]*)\"$")
    public void is(String sOrder, String sEtat) {
        Assert.assertEquals(context.orders.get(sOrder).getState(), context.utils.stateFromName(sEtat));
    }

    @And("^\"([^\"]*)\" receveid a \"([^\"]*)\"$")
    public void receveidA(String arg0, String arg1) {
        // Write code here that turns the phrase above into concrete actions
    }


}
