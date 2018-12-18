package StepDefinitions;

import cucumber.api.java.en.And;
import main.Guest;
import order.Order;
import utils.CucumberContext;
import utils.TestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;


public class AnonymousOrderStepDefs {

    private final CucumberContext context = CucumberContext.getContext();
    private Order currentOrder;
    private final TestUtils utils = new TestUtils();

    @And("^The customer entered her \"([^\"]*)\" to place her order and pay \"([^\"]*)\"$")
    public void theCustomerEnteredHerToPlaceHerOrderAndPay(String sEmail, String online) {
        Optional<Guest> guest = context.cookieFirm().findGuestOrCustomer(context.getCurrentId());
        if (guest.isPresent()) {
            currentOrder = guest.get().getTemporaryOrder();
            context.getFacade().guestValidateHisOrder(context.getCurrentId(), sEmail, utils.payOnline(online));
        }
    }

    @And("^\"([^\"]*)\" pay her cookies$")
    public void payHerCookies(String customerName) {
        Optional<Guest> opClient = context.cookieFirm().findGuestOrCustomer(context.getCurrentId());

        if (opClient.isPresent())
            currentOrder.setBankingData(opClient.get().getBankingData());
        else
            throw new IllegalStateException("There is no current client in the cookiefirm");
    }

    @And("^The order is \"([^\"]*)\"$")
    public void theOrderIs(String sEtat) {
        assertEquals(currentOrder.getState(), context.utils.stateFromName(sEtat));
    }

}
