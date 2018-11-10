package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Guest;
import main.Store;
import order.Order;
import order.OrderState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OrderWithdrawal {

    private Map<String, Guest> guests = new HashMap<String, Guest>();
    private Map<String, Order> orders = new HashMap<String, Order>();
    private Order currentOrder;
    private Store store;

    @Given("^A customer \"([^\"]*)\"$")
    public void aCustomer(String name) throws Throwable {
        guests.put(name, new Guest());
    }

    @Given("^An order \"([^\"]*)\"$")
    public void anOrder(String name) throws Throwable {
        orders.put(name, new Order());
    }

    @Given("^the customer \"([^\"]*)\" has paid for \"([^\"]*)\"$")
    public void theCustomerHasPaidFor(String customerName, String orderName) throws Throwable {
        orders.get(orderName).setOrderState(OrderState.ORDERED);
        orders.get(orderName).pay();
    }

    @When("^the employee scans \"([^\"]*)\"$")
    public void theEmployeeScans(String orderName) throws Throwable {
        final Order targetOrder = orders.get(orderName);
        currentOrder = store
                .findOrder(targetOrder.getPickUpTime(), targetOrder.getPickupDay(), targetOrder.getGuest().getEmail())
                .orElse(null);
    }


    @And("^the empoyee recieves \"([^\"]*)\" payment$")
    public void theEmpoyeeRecievesPayment(String customerName) throws Throwable {
        currentOrder.pay();
    }

    @Then("^the order \"([^\"]*)\" state is \"([^\"]*)\"$")
    public void theOrderStatusIs(String orderName, String targetStateName) throws Throwable {
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
        assertEquals(orders.get(orderName).getState(), targetState);
    }

    @Given("^A store$")
    public void aStore() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        store = new Store(null, Collections.emptyList(), Collections.emptyList(), null, null, 1.0);
    }

    @Given("^\"([^\"]*)\" is passed in the store by \"([^\"]*)\"$")
    public void isPassedInTheStoreBy(String orderName, String clientName) throws Throwable {
        final Order order = orders.get(orderName);
        order.setGuest(guests.get(clientName));
        store.placeOrder(order);
    }

    @Given("^the customer \"([^\"]*)\" has the order \"([^\"]*)\"$")
    public void theCustomerHasTheOrder(String customerName, String orderName) throws Throwable {
        orders.get(orderName).setGuest(guests.get(customerName));
    }


    @When("^the employee delivers the current order$")
    public void theEmployeeDeliversTheCurrentOrder() throws Throwable {
        currentOrder.withdraw();
    }
}
