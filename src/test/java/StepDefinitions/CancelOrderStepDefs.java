package StepDefinitions;

import cucumber.api.java.en.And;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

public class CancelOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @And("^An employee see the \"([^\"]*)\"'s orders$")
    public void anEmployeeSeeTheSOrders(String sStore) {
    }

    @And("^\"([^\"]*)\" receveid an email at \"([^\"]*)\"$")
    public void receveidA(String arg0, String arg1) {
        // Write code here that turns the phrase above into concrete actions
    }


}
