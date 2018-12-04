package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import main.Guest;
import order.Order;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


public class AnonymousOrderStepDefs {

    private final CucumberContext context= CucumberContext.getContext();
    private Order currentOrder;
    private TestUtils utils = new TestUtils();


    @And("^The customer entered her \"([^\"]*)\" to place her order and pay \"([^\"]*)\"$")
    public void theCustomerEnteredHerToPlaceHerOrderAndPay(String sEmail, String online) throws Throwable {
        Optional<Guest> guest = context.cookieFirm().findGuest(context.getCurrentId());
        if(guest.isPresent()) {
            currentOrder = guest.get().getTemporaryOrder();
            context.getFacade().guestValidateHisOrder(context.getCurrentId(), sEmail, utils.payOnline(online));
        }

    }

    @And("^\"([^\"]*)\" pay her cookies$")
    public void payHerCookies(String sGuest) {
        currentOrder.setPayed();
    }

    @And("^The order is \"([^\"]*)\"$")
    public void theOrderIs(String sEtat) {
       assertEquals(currentOrder.getState(), context.utils.stateFromName(sEtat));
    }



}
