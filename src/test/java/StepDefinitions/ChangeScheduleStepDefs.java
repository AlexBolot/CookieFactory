package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Day;
import main.Manager;
import main.Store;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ChangeScheduleStepDefs {

    private Map<String, Manager> managers = new HashMap<>();
    private Map<String, Store> stores = new HashMap<>();

    private Exception openingTimeException;
    private Exception closingTimeException;

    private Day dayFromName(String dayName) {
        for (Day day : Day.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }

    @Given("^A store \"([^\"]*)\"$")
    public void aStore(String storeName) {
        stores.put(storeName, new Store(15));
    }

    @Given("^\"([^\"]*)\" the manager of \"([^\"]*)\"$")
    public void theManagerOf(String managerName, String storeName) {
        Manager manager = new Manager(stores.get(storeName));
        managers.put(managerName, manager);
    }

    @Given("^The \"([^\"]*)\" opens \"([^\"]*)\" at \"([^\"]*)\" and closes at \"([^\"]*)\"$")
    public void theStoreOpensAtAndClosesAt(String storeName, String dayName, String openingTime, String closingTime) {

        //Given the template Hours:Minutes
        int opHour = Integer.parseInt(openingTime.split(":")[0]);
        int opMinutes = Integer.parseInt(openingTime.split(":")[1]);

        int clHour = Integer.parseInt(closingTime.split(":")[0]);
        int clMinutes = Integer.parseInt(closingTime.split(":")[1]);

        Day day = dayFromName(dayName);

        LocalDateTime opTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(opHour, opMinutes));
        LocalDateTime clTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(clHour, clMinutes));

        Store store = stores.get(storeName);

        store.setOpeningTime(day, opTime);
        store.setClosingTimes(day, clTime);
    }

    @When("^\"([^\"]*)\" changes opening time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesOpeningTime(String managerName, String dayName, String openingTime) {

        //Given the template Hours:Minutes
        int opHour = Integer.parseInt(openingTime.split(":")[0]);
        int opMinutes = Integer.parseInt(openingTime.split(":")[1]);

        Day day = dayFromName(dayName);

        LocalDateTime opTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(opHour, opMinutes));

        Manager manager = managers.get(managerName);

        try {
            manager.changeOpeningTime(day, opTime);

            if (opTime.isAfter(manager.getStore().closingTime(day)))
                Assert.fail("Expecting IllegalArgumentException to be thrown");

        } catch (IllegalArgumentException iae) {
            openingTimeException = iae;
        }
    }

    @Then("^The \"([^\"]*)\" opening of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theStoreOpeningIs(String storeName, String dayName, String openingTime) {

        //Given the template Hours:Minutes
        int opHour = Integer.parseInt(openingTime.split(":")[0]);
        int opMinutes = Integer.parseInt(openingTime.split(":")[1]);

        Day day = dayFromName(dayName);

        LocalDateTime expectedOpTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(opHour, opMinutes));

        Assert.assertEquals(expectedOpTime, stores.get(storeName).openingTime(day));
    }

    @When("^\"([^\"]*)\" changes closing time of \"([^\"]*)\" to \"([^\"]*)\"$")
    public void theManagerChangesClosingTime(String managerName, String dayName, String closingTime) {
        //Given the template Hours:Minutes
        int opHour = Integer.parseInt(closingTime.split(":")[0]);
        int opMinutes = Integer.parseInt(closingTime.split(":")[1]);

        Day day = dayFromName(dayName);

        LocalDateTime clTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(opHour, opMinutes));

        Manager manager = managers.get(managerName);

        try {
            manager.changeClosingTime(day, clTime);

            if (clTime.isBefore(manager.getStore().openingTime(day)))
                Assert.fail("Expecting IllegalArgumentException to be thrown");

        } catch (IllegalArgumentException iae) {
            closingTimeException = iae;
        }
    }

    @Then("^The \"([^\"]*)\" closing of \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theStoreClosingIs(String storeName, String dayName, String closingTime) {

        //Given the template Hours:Minutes
        int clHour = Integer.parseInt(closingTime.split(":")[0]);
        int clMinutes = Integer.parseInt(closingTime.split(":")[1]);

        Day day = dayFromName(dayName);

        LocalDateTime expectedClTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(clHour, clMinutes));

        Assert.assertEquals(expectedClTime, stores.get(storeName).closingTime(day));
    }

    @Then("^Changing opening time of \"([^\"]*)\" fails$")
    public void changingOpeningTimeFails(String dayName) {
        Exception exception = new IllegalArgumentException("Trying to set opening time after closing time for " + dayFromName(dayName));
        Assert.assertEquals(openingTimeException.getMessage(), exception.getMessage());
        openingTimeException = null;
    }

    @Then("^Changing closing time of \"([^\"]*)\" fails$")
    public void changingClosingTimeFails(String dayName) {
        Exception exception = new IllegalArgumentException("Trying to set closing time before opening time for " + dayFromName(dayName));
        Assert.assertEquals(closingTimeException.getMessage(), exception.getMessage());
        closingTimeException = null;
    }
}
