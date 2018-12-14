package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Guest;
import order.Order;
import order.OrderState;
import store.Store;
import utils.CucumberContext;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderWithdrawalStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Order currentOrder;
    private Exception illegalWithdraw;

    @Given("^An order \"([^\"]*)\"$")
    public void anOrder(String name) {
        context.orders.put(name, new Order(null, LocalDateTime.now()));
    }

    @Given("^the customer \"([^\"]*)\" has paid for \"([^\"]*)\"$")
    public void theCustomerHasPaidFor(String customerName, String orderName) {
        Order order = context.orders.get(orderName);
        order.placeOrder();

        context.cookieFirm().setBankAPI(context.cookieFirm().getBankAPI());
    }

    @When("^the employee of \"([^\"]*)\" scans \"([^\"]*)\"$")
    public void theEmployeeScans(String storeName, String orderName) {
        final Order targetOrder = context.orders.get(orderName);
        final Store store = context.stores.get(storeName);
        currentOrder = store
                .findOrder(targetOrder.getPickUpTime(), targetOrder.getGuest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Not found the order"));
    }

    @Given("^\"([^\"]*)\" is passed in the store \"([^\"]*)\" by \"([^\"]*)\"$")
    public void isPassedInTheStoreBy(String orderName, String storeName, String clientName) {
        final Order order = context.orders.get(orderName);
        final Guest guest = context.getGuest(clientName);
        final Store store = context.stores.get(storeName);

        order.setGuest(guest);
        order.setStore(store);
        store.getOrders().add(order);
    }

    @Given("^the customer \"([^\"]*)\" has the order \"([^\"]*)\"$")
    public void theCustomerHasTheOrder(String customerName, String orderName) {
        Order order = context.orders.get(orderName);
        order.setGuest(context.getGuest(customerName));
        order.placeOrder();
    }

    @When("^the employee delivers the current order$")
    public void theEmployeeDeliversTheCurrentOrder() {
        try {
            currentOrder.withdraw();
        } catch (Exception e) {
            illegalWithdraw = e;
        }
    }

    @Then("^The current order state is \"([^\"]*)\"$")
    public void theCurrentOrderStateIs(String targetStateName) {
        OrderState targetState = context.utils.stateFromName(targetStateName);
        assertEquals(currentOrder.getState(), targetState);
    }

    @Then("^The current order state is not \"([^\"]*)\"$")
    public void theCurrentOrderStateIsnot(String targetStateName) {
        OrderState targetState = context.utils.stateFromName(targetStateName);
        assertNotEquals(currentOrder.getState(), targetState);
        assertEquals(illegalWithdraw.getMessage(), "Trying to withdraw an unpayed order !");
    }
}
