package main;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NewAccountStepDefs {
    @Given("^The customer wants to place his order$")
    public void theCustomerWantsToPlaceHisOrder() throws Throwable {

    }

    @And("^The customer gives his \"([^\"]*)\"$")
    public void theCustomerGivesHis(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^The customer \"([^\"]*)\" of an account$")
    public void theCustomerOfAnAccount(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^An account is created with the supplied \"([^\"]*)\"$")
    public void anAccountIsCreatedWithTheSupplied(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^The password is saved$")
    public void thePasswordIsSaved() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^The order is placed$")
    public void theOrderIsPlaced() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
