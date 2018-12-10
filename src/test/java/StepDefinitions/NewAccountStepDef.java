package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Customer;
import order.Order;
import store.Store;
import utils.CucumberContext;

import java.util.Optional;

public class NewAccountStepDef {

    private final CucumberContext context = CucumberContext.getContext();


    @When("^The guest create an account at the name of \"([^\"]*)\" \"([^\"]*)\" with the password \"([^\"]*)\" and the phone \"([^\"]*)\" from \"([^\"]*)\"$")
    public void theGuestCreateAnAccountAtTheNameOfWithThePasswordAndThePhoneFrom(String name, String firstName,
                                                                                 String password,
                                                                                 String phone, String email) throws Throwable {
        context.getFacade().guestCreateAccount(context.getCurrentId(), name, firstName, phone,email, password);
    }

    @Then("^The account with \"([^\"]*)\" is saved$")
    public void theAccountIsSaved(String email) {
        Optional<Customer> customerOptional = context.cookieFirm().findCustomer(email);

        if(customerOptional.isPresent())
            assert(true);
        else
            assert(false);
    }


    @And("^The order in the \"([^\"]*)\" with (\\d+), \"([^\"]*)\" made by \"([^\"]*)\" is saved$")
    public void theOrderInTheWithMadeByIsSaved(String store, int hour, String day, String email) throws Throwable {
        Optional<Store> storeOp= context.cookieFirm().findStore(store);

        if(storeOp.isPresent()){
            Optional<Order> order = storeOp.get().findOrder(context.getFacade().generateTime(hour, day),email);
            if(order.isPresent())
                assert(true);
        }

    }
}
