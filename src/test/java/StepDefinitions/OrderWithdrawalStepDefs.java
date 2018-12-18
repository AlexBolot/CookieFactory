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
import java.util.Optional;

import static org.junit.Assert.*;

public class OrderWithdrawalStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Order currentOrder;

    @When("^the employee delivers the current order$")
    public void theEmployeeDeliversTheCurrentOrder() {
        try {
            currentOrder.withdraw();
        } catch (Exception e) {
            context.pushException(e);
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

        Optional<Exception> optionalException = context.popException();

        assertTrue(optionalException.isPresent());
        assertEquals(optionalException.get().getMessage(), "Trying to withdraw an unpayed order !");
    }
}
