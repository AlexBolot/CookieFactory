package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import order.Order;
import org.junit.Assert;
import store.Store;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CancelOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Collection<Order> ordersStore = new ArrayList<>();
    private Order currentOrder;


    @And("^An employee see the \"([^\"]*)\"'s orders$")
    public void anEmployeeSeeTheSOrders(String sStore) {
        Optional<Store> store = context.cookieFirm().findStore(sStore);
        if(store.isPresent()){
           //TODO permettre de voir les recettes d'un store.get().
        }
    }

    @And("^\"([^\"]*)\" receveid a \"([^\"]*)\"$")
    public void receveidA(String arg0, String arg1) {
        // Write code here that turns the phrase above into concrete actions
    }


}
