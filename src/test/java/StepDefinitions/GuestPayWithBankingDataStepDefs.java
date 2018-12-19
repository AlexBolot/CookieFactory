package StepDefinitions;

import cucumber.api.java.en.Then;
import org.junit.Assert;
import utils.CucumberContext;

import java.util.Optional;

public class GuestPayWithBankingDataStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @Then("^The customer order is not validated$")
    public void theCustomerOrderIsNotValidated() {

        Exception exception = new IllegalArgumentException("Banking Data does not exist");
        Optional<Exception> exception1 = context.popException();
        if(exception1.isPresent()) {
            Assert.assertEquals(exception.getMessage(),exception1.get().getMessage());
        }

    }
}
