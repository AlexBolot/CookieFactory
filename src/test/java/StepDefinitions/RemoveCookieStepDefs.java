package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RemoveCookieStepDefs {
    @Given("^The \"([^\"]*)\" checks his order$")
    public void theChecksHisOrder(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^The \"([^\"]*)\" select (\\d+) cookie to remove$")
    public void theSelectCookieToRemove(String arg0, int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the order contains (\\d+) cookies less$")
    public void theOrderContainsCookiesLess(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
