package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import main.Customer;
import utils.CucumberContext;
import utils.TestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomerOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();
    private final TestUtils utils = new TestUtils();

    @Then("^The customer with the email \"([^\"]*)\" has (\\d+) order in his history$")
    public void theCustomerWithTheEmailHasOrderInHisHistory(String email, int amount) {
        Optional<Customer> opCustomer = context.cookieFirm().findCustomer(email);
        if (opCustomer.isPresent())
            assertEquals(amount, opCustomer.get().getOrderHistory().size());
        else throw new IllegalStateException("Could not find current Customer");
    }

    @And("^The customer with the email \"([^\"]*)\" has an empty temporary order$")
    public void theCustomerWithTheEmailHasAnEmptyTemporaryOrder(String email) {
        Optional<Customer> opCustomer = context.cookieFirm().findCustomer(email);
        if (opCustomer.isPresent())
            assertEquals(0, opCustomer.get().getTemporaryOrder().getOrderLines().size());
        else throw new IllegalStateException("Could not find current Customer");
    }

    @Then("^It fails because it's a too short delay$")
    public void itFailsBecauseItSATooShortDelay() {
        String message = "The pickup time is in less than 2h";

        Optional<Exception> optionalException = context.popException();

        assertTrue(optionalException.isPresent());
        assertEquals(message, optionalException.get().getMessage());
    }

    @Then("^It fails because it's before opening$")
    public void itFailsBecauseItSBeforeOpening() {
        String message = "The pickup time is earlier than opening time of the store";

        Optional<Exception> optionalException = context.popException();

        assertTrue(optionalException.isPresent());
        assertEquals(message, optionalException.get().getMessage());
    }

    @Then("^It fails because it's after closing$")
    public void itFailsBecauseItSAfterClosing() {
        String message = "The pickup time is later than closing time of the store";

        Optional<Exception> optionalException = context.popException();

        assertTrue(optionalException.isPresent());
        assertEquals(message, optionalException.get().getMessage());
    }
}


