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

    @Given("^The store managed by \"([^\"]*)\" opens \"([^\"]*)\" at \"([^\"]*)\" and closes at \"([^\"]*)\"$")
    public void theStoreManagedByOpensAtAndClosesAt(String mName, String sDay, String opening, String closing) {
        context.getFacade().managerChangeOpeningClosingTime(mName, sDay, opening, closing);
    }

    @When("^\"([^\"]*)\" changes opening time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesOpeningTime(String managerName, String dayName, String openingTime) {
        try {
            context.getFacade().managerChangeOpeningTime(managerName, dayName, openingTime);
        } catch (IllegalArgumentException iae) {
            context.pushException(iae);
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
            context.pushException(iae);
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

        Optional<Exception> optionalException = context.popException();

        assertTrue(optionalException.isPresent());
        assertTrue(optionalException.get().getMessage().equalsIgnoreCase(message));
    }

    @Then("^Changing closing time of \"([^\"]*)\" fails$")
    public void changingClosingTimeFails(String dayName) {
        String message = "Trying to set closing time before opening time for " + dayName;

        Optional<Exception> optionalException = context.popException();

        assertTrue(optionalException.isPresent());
        assertTrue(optionalException.get().getMessage().equalsIgnoreCase(message));
    }


}
