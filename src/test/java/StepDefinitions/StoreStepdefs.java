package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import store.Manager;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StoreStepdefs {

    private final CucumberContext context = CucumberContext.getContext();

    @Then("^The cookieFirm as one store name \"([^\"]*)\" with a manager named \"([^\"]*)\"$")
    public void theCookieFirmAsOneStoreNameWithAManagerNamed(String storeName, String managerName) throws Throwable {

        Optional<Store> store = context.cookieFirm().findStore(storeName);
        Optional<Manager> manager = context.cookieFirm().findManager(managerName);
        if(store.isPresent() && manager.isPresent()){
            assertEquals(store.get().getName(), storeName);
            assertEquals(manager.get().getStore(), store.get());
        }
    }

    @When("^\"([^\"]*)\" change the tax of her store to (.+)$")
    public void changeTheTaxOfHerStoreTo(String manager, double tax) throws Throwable {
        context.getFacade().managerChangeTaxeOfStore(manager, tax);

    }

    @Then("^The store \"([^\"]*)\" have a tax of (.+)$")
    public void theStoreHaveATaxOf(String nameStore, double tax) throws Throwable {
        context.cookieFirm().findStore(nameStore).ifPresent(store1 -> assertEquals(store1.getTax(), tax, 0));
    }
}
