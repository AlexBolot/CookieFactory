package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Day;
import main.Guest;
import main.Store;
import order.Order;
import order.OrderState;
import utils.CucumberContext;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderWithdrawalStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Order currentOrder;
    private Exception illegalWithdraw;

    private OrderState getStateFromName(String targetStateName) {
        OrderState targetState = null;
        switch (targetStateName) {
            case "Draft":
                targetState = OrderState.DRAFT;
                break;
            case "Ordered":
                targetState = OrderState.ORDERED;
                break;
            case "Canceled":
                targetState = OrderState.CANCELED;
                break;
            case "Withdrawn":
                targetState = OrderState.WITHDRAWN;
        }
        return targetState;
    }

    @Given("^An order \"([^\"]*)\"$")
    public void anOrder(String name) {
        context.orders.put(name, new Order(null, LocalDateTime.now(), Day.TUESDAY));
    }

    @Given("^the customer \"([^\"]*)\" has paid for \"([^\"]*)\"$")
    public void theCustomerHasPaidFor(String customerName, String orderName) {
        Order order = context.orders.get(orderName);
        order.placeOrder();
        order.pay();
    }

    @When("^the employee of \"([^\"]*)\" scans \"([^\"]*)\"$")
    public void theEmployeeScans(String storeName, String orderName) {
        final Order targetOrder = context.orders.get(orderName);
        final Store store = context.stores.get(storeName);
        currentOrder = store
                .findOrder(targetOrder.getPickUpTime(), targetOrder.getPickupDay(), targetOrder.getGuest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Not found the order"));
    }

    @And("^the employee recieves \"([^\"]*)\" payment$")
    public void theEmpoyeeRecievesPayment(String customerName) {
        currentOrder.pay();
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
        OrderState targetState = getStateFromName(targetStateName);
        assertEquals(currentOrder.getState(), targetState);
    }

    @Then("^The current order state is not \"([^\"]*)\"$")
    public void theCurrentOrderStateIsnot(String targetStateName) {
        OrderState targetState = getStateFromName(targetStateName);
        assertNotEquals(currentOrder.getState(), targetState);
        assertEquals(illegalWithdraw.getMessage(), "Trying to withdraw an unpayed order !");
    }
}
