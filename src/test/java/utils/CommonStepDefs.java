package utils;

import cucumber.api.java.en.Given;
import main.*;
import order.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class CommonStepDefs {

    private final CucumberContext context = CucumberContext.getContext();

    @Given("^A store \"([^\"]*)\"$")
    public void aStore(String storeName) {
        context.stores.put(storeName, new Store(null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1));
    }

    @Given("^A customer \"([^\"]*)\"$")
    public void aCustomer(String name) {
        CookieFirm cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());
        Customer customer = cookieFirm.createAccount("", "", "", "", "", new Order());
        context.addGuest(name, customer);
    }

    @Given("^A guest \"([^\"]*)\"$")
    public void aGuest(String name) {
        context.addGuest(name, new Guest(name));
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

        Day day = context.utils.dayFromName(dayName);

        LocalDateTime opTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(opHour, opMinutes));
        LocalDateTime clTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(clHour, clMinutes));

        Store store = context.stores.get(storeName);

        store.setOpeningTime(day, opTime);
        store.setClosingTime(day, clTime);
    }
}
