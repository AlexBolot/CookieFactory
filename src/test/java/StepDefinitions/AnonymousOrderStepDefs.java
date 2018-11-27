package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import main.Guest;
import order.Order;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.Assert.assertEquals;


public class AnonymousOrderStepDefs {

    private final CucumberContext context= CucumberContext.getContext();
    private Order currentOrder;

    @Given("^A guest \"([^\"]*)\" have selected (\\d+) cookies in the \"([^\"]*)\"$")
    public void aGuestHaveSelectedCookiesInThe(String sName, int iCookies, String sStore) {

        context.addGuest(sName, new Guest());
        Guest guest = context.getGuest(sName);
        Order order = guest.getTemporaryOrder();
        order.setStore(context.getStore(sStore));
        order.addCookie(context.utils.randomRecipe(), iCookies);
    }

    @And("^\"([^\"]*)\" choose to pickup her \"([^\"]*)\" in (\\d+) hours in the \"([^\"]*)\" on \"([^\"]*)\" and want to pay \"([^\"]*)\"$")
    public void chooseToPickupHerInHoursInTheOnAndWantToPayInThe(String sName, String sOrderName, int hours, String
            sStore, String sDay, String payment) {
        Guest guest  = context.getGuest(sName);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(sDay.toUpperCase());
        LocalDateTime pickTime = LocalDateTime.now().plusHours(hours).with(TemporalAdjusters.next(dayOfWeek));

        guest.getTemporaryOrder().setStore(context.stores.get(sStore));
        guest.getTemporaryOrder().setPickUpTime(pickTime);
        context.orders.put(sOrderName, context.getGuest(sName).getTemporaryOrder());

        if(payment.equals("online"))
            guest.placeOrder(true);
        else
            guest.placeOrder(false);
    }

    @And("^\"([^\"]*)\" entered her \"([^\"]*)\" to put her \"([^\"]*)\"$")
    public void enteredHerToPutHer(String sName, String sEmail, String sOrderName) {
        context.getGuest(sName).setEmail(sEmail);
    }

    @Then("^The purchase \"([^\"]*)\" is scan in the \"([^\"]*)\"$")
    public void thePurchaseIsScanInThe(String sOrderName, String sStore) {
        final Order targetOrder = context.orders.get(sOrderName);
        currentOrder = context.stores.get(sStore)
                .findOrder(targetOrder.getPickUpTime(), targetOrder.getGuest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Not found the order"));
    }


    @And("^\"([^\"]*)\" pay her cookies$")
    public void payHerCookies(String sGuest) {
        currentOrder.setPayed();
        currentOrder.withdraw();
    }

    @And("^The order is \"([^\"]*)\"$")
    public void theOrderIs(String sEtat) {
       assertEquals(currentOrder.getState(), context.utils.stateFromName(sEtat));
    }



}
