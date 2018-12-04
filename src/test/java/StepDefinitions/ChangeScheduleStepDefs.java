package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import store.Manager;
import store.Store;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

public class ChangeScheduleStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Exception openingTimeException;
    private Exception closingTimeException;

    @Given("^The store manage by \"([^\"]*)\" opens \"([^\"]*)\" at \"([^\"]*)\" and closes at \"([^\"]*)\"$")
    public void theStoreManageByOpensAtAndClosesAt(String mName, String sDay, String opening, String closing) throws Throwable {
        context.getFacade().addOpeningClosingTime(mName, sDay, opening, closing);
    }

    @When("^\"([^\"]*)\" changes opening time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesOpeningTime(String managerName, String dayName, String openingTime) {
        context.getFacade().managerChangeOpeningTime(managerName, dayName, openingTime);
    }

    @Then("^The \"([^\"]*)\" opening of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theStoreOpeningIs(String storeName, String dayName, String openingTime) {

        //Given the template Hours:Minutes
        int opHour = Integer.parseInt(openingTime.split(":")[0]);
        int opMinutes = Integer.parseInt(openingTime.split(":")[1]);

        DayOfWeek day = context.utils.dayFromName(dayName);

        LocalTime expectedOpTime = LocalTime.of(opHour, opMinutes);

        Optional<Store> store = context.cookieFirm().findStore(storeName);
        if (store.isPresent())
            Assert.assertEquals(expectedOpTime, store.get().openingTime(day));
    }

    @When("^\"([^\"]*)\" changes closing time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesClosingTime(String managerName, String dayName, String closingTime) {
        context.getFacade().managerChangeClosingTime(managerName, dayName, closingTime);
    }

    @Then("^The \"([^\"]*)\" closing of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theStoreClosingIs(String storeName, String dayName, String closingTime) {

        //Given the template Hours:Minutes
        int clHour = Integer.parseInt(closingTime.split(":")[0]);
        int clMinutes = Integer.parseInt(closingTime.split(":")[1]);

        DayOfWeek day = context.utils.dayFromName(dayName);

        LocalTime expectedClTime = LocalTime.of(clHour, clMinutes);

        Optional<Store> store = context.cookieFirm().findStore(storeName);
        if(store.isPresent())
            Assert.assertEquals(expectedClTime, store.get().closingTime(day));
    }

    @Then("^Changing opening time of \"([^\"]*)\" fails$")
    public void changingOpeningTimeFails(String dayName) {
        Exception exception = new IllegalArgumentException("Trying to set opening time after closing time for " + context.utils.dayFromName(dayName));
        Assert.assertEquals(openingTimeException.getMessage(), exception.getMessage());
        openingTimeException = null;
    }

    @Then("^Changing closing time of \"([^\"]*)\" fails$")
    public void changingClosingTimeFails(String dayName) {
        Exception exception = new IllegalArgumentException("Trying to set closing time before opening time for " + context.utils.dayFromName(dayName));
        Assert.assertEquals(closingTimeException.getMessage(), exception.getMessage());
        closingTimeException = null;
    }



}
