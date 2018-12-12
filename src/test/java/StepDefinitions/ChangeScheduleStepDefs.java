package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import store.Store;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeScheduleStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    private Exception openingTimeException;
    private Exception closingTimeException;

    @Given("^The store manage by \"([^\"]*)\" opens \"([^\"]*)\" at \"([^\"]*)\" and closes at \"([^\"]*)\"$")
    public void theStoreManageByOpensAtAndClosesAt(String mName, String sDay, String opening, String closing) throws Throwable {
        context.getFacade().managerChangeOpeningClosingTime(mName, sDay, opening, closing);
    }

    @When("^\"([^\"]*)\" changes opening time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesOpeningTime(String managerName, String dayName, String openingTime) {
        try {
            context.getFacade().managerChangeOpeningTime(managerName, dayName, openingTime);
        } catch (IllegalArgumentException iae) {
            this.openingTimeException = iae;
        }
    }

    @Then("^The \"([^\"]*)\" opening of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theStoreOpeningIs(String storeName, String dayName, String openingTime) {

        //Given the template Hours:Minutes
        int clHour = Integer.parseInt(openingTime.split(":")[0]);
        int clMinutes = Integer.parseInt(openingTime.split(":")[1]);

        DayOfWeek day = DayOfWeek.valueOf(dayName.toUpperCase());

        LocalTime expectedClTime = LocalTime.of(clHour, clMinutes);

        Optional<Store> store = context.cookieFirm().findStore(storeName);
        if (store.isPresent())
            assertEquals(expectedClTime, store.get().openingTime(day));
        else throw new IllegalStateException("Store named " + storeName + " not found");
    }

    @When("^\"([^\"]*)\" changes closing time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesClosingTime(String managerName, String dayName, String closingTime) {
        try {
            context.getFacade().managerChangeClosingTime(managerName, dayName, closingTime);
        } catch (IllegalArgumentException iae) {
            this.closingTimeException = iae;
        }
    }

    @Then("^The \"([^\"]*)\" closing of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theStoreClosingIs(String storeName, String dayName, String closingTime) {

        //Given the template Hours:Minutes
        int clHour = Integer.parseInt(closingTime.split(":")[0]);
        int clMinutes = Integer.parseInt(closingTime.split(":")[1]);

        DayOfWeek day = DayOfWeek.valueOf(dayName.toUpperCase());

        LocalTime expectedClTime = LocalTime.of(clHour, clMinutes);

        Optional<Store> store = context.cookieFirm().findStore(storeName);
        if (store.isPresent())
            assertEquals(expectedClTime, store.get().closingTime(day));
        else throw new IllegalStateException("Store named " + storeName + " not found");
    }

    @Then("^Changing opening time of \"([^\"]*)\" fails$")
    public void changingOpeningTimeFails(String dayName) {
        String message = "Trying to set opening time after closing time for " + dayName;
        assertTrue(openingTimeException.getMessage().equalsIgnoreCase(message));
        openingTimeException = null;
    }

    @Then("^Changing closing time of \"([^\"]*)\" fails$")
    public void changingClosingTimeFails(String dayName) {
        String message = "Trying to set closing time before opening time for " + dayName;
        assertTrue(closingTimeException.getMessage().equalsIgnoreCase(message));
        closingTimeException = null;
    }


}
