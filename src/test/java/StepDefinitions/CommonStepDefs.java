package StepDefinitions;

import api.BankingData;
import cucumber.api.java.en.Given;
import main.Customer;
import main.Guest;
import order.Order;
import store.Manager;
import store.Store;
import utils.CucumberContext;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static utils.TestUtils.getInfiniteMockKitchen;

public class CommonStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @Given("^A store \"([^\"]*)\"$")
    public void aStore(String storeName) {
        Store store = new Store("", null, new ArrayList<>(), new HashMap<>(),
                new HashMap<>(), 1, 1);
        store.setKitchen(getInfiniteMockKitchen());

        context.stores.put(storeName, store);
    }

    @Given("^A customer \"([^\"]*)\"$")
    public void aCustomer(String name) {
        BankingData bankingData = new BankingData(name, name, "58493849583");

        Customer customer = context.getCookieFirm().createAccount(name, name, "0638493756", name+"@"+name+".fr", name+"1234", new Order());
        customer.setBankingData(bankingData);
        context.addGuest(name, customer);
    }

    @Given("^A guest \"([^\"]*)\"$")
    public void aGuest(String name) {
        context.addGuest(name, new Guest());
    }

    @Given("^\"([^\"]*)\" the manager of \"([^\"]*)\"$")
    public void theManagerOf(String managerName, String storeName) {
        Manager manager = new Manager(context.stores.get(storeName));
        context.managers.put(managerName, manager);
    }

    @Given("^The \"([^\"]*)\" opens \"([^\"]*)\" at \"([^\"]*)\" and closes at \"([^\"]*)\"$")
    public void theStoreOpensAtAndClosesAt(String storeName, String dayName, String openingTime, String closingTime) {

        //Given the template Hours:Minutes
        int opHour = Integer.parseInt(openingTime.split(":")[0]);
        int opMinutes = Integer.parseInt(openingTime.split(":")[1]);

        int clHour = Integer.parseInt(closingTime.split(":")[0]);
        int clMinutes = Integer.parseInt(closingTime.split(":")[1]);

        DayOfWeek day = context.utils.dayFromName(dayName);

        LocalTime opTime = LocalTime.of(opHour, opMinutes);
        LocalTime clTime = LocalTime.of(clHour, clMinutes);

        Store store = context.stores.get(storeName);

        store.setOpeningTime(day, opTime);
        store.setClosingTime(day, clTime);
    }

    @Given("^The kitchen for \"([^\"]*)\" is infinite$")
    public void theKitchenForIsInfinite(String storeName) {
        context.getStore(storeName).setKitchen(getInfiniteMockKitchen());
    }
}
