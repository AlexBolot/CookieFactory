package StepDefinitions;

import cucumber.api.java.en.And;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

public class CancelOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @And("^An employee see the \"([^\"]*)\"'s orders$")
    public void anEmployeeSeeTheSOrders(String sStore) {
        Optional<Store> store = context.cookieFirm().findStore(sStore);
        if (store.isPresent()) {
            //TODO permettre de voir les recettes d'un store.get().
        }
    }

    @And("^\"([^\"]*)\" receveid a \"([^\"]*)\"$")
    public void receveidA(String arg0, String arg1) {
        // Write code here that turns the phrase above into concrete actions
    }


}
