package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import order.Order;
import org.junit.Assert;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

public class NewAccountStepDef {

    private final CucumberContext context = CucumberContext.getContext();


    @When("^The guest create an account at the name of \"([^\"]*)\" \"([^\"]*)\" with the password \"([^\"]*)\" and the phone \"([^\"]*)\" from \"([^\"]*)\"$")
    public void theGuestCreateAnAccountAtTheNameOfWithThePasswordAndThePhoneFrom(String name, String firstName,
                                                                                 String password,
                                                                                 String phone, String email) {
        context.getFacade().guestCreateAccount(context.getCurrentId(), name, firstName, phone,email, password);
    }

    @Then("^The account with \"([^\"]*)\" is saved$")
    public void theAccountIsSaved(String email) {
        Optional<Customer> customerOptional = context.cookieFirm().findCustomer(email);
        Assert.assertTrue(customerOptional.isPresent());
    }

    @And("^The order in the \"([^\"]*)\" with (\\d+):(\\d+), \"([^\"]*)\" made by \"([^\"]*)\" is saved$")
    public void theOrderInTheWithMadeByIsSaved(String store, int hours, int minutes, String day, String email) {
        Optional<Store> storeOp= context.cookieFirm().findStore(store);

        if(storeOp.isPresent()){
            Optional<Order> order = storeOp.get().findOrder(context.getFacade().generateTime(hours, minutes, day),email);
            Assert.assertTrue(order.isPresent());
        }

    }
}
