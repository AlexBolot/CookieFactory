package StepDefinitions;

import api.BankingData;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import main.Customer;
import main.Facade;
import main.Guest;
import order.Order;
import store.Manager;
import store.Store;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static utils.TestUtils.getInfiniteMockKitchen;

public class CommonStepDefs {

    private final CucumberContext context = CucumberContext.getContext();
    private TestUtils utils = new TestUtils();

    @Given("^A store \"([^\"]*)\"$")
    public void aStore(String storeName) {
        Store store = new Store("", null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(),
                new HashMap<>(), 1);
        store.setKitchen(getInfiniteMockKitchen());

        context.stores.put(storeName, store);
    }

    @Given("^A customer \"([^\"]*)\"$")
    public void aCustomer(String name) {
        context.getFacade().createACustomer(name, name, "07475674", utils.createEmail(name), name+"1234");
        context.getFacade().setBankingData(utils.createEmail(name), "02833777");
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

    @Given("^The \"([^\"]*)\" opens \"([^\"]*)\" (\\d+) hours ago and closes in (\\d+) hours$")
    public void theStoreOpensHoursAgoAndClosesInHours(String storeName, String dayName, int behindHours, int aheadHours) {

        DayOfWeek day = context.utils.dayFromName(dayName);

        LocalTime opTime = LocalTime.now().minusHours(behindHours);
        LocalTime clTime = LocalTime.now().plusHours(aheadHours);

        Store store = context.stores.get(storeName);

        store.setOpeningTime(day, opTime);
        store.setClosingTime(day, clTime);
    }


    @Given("^The kitchen for \"([^\"]*)\" is infinite$")
    public void theKitchenForIsInfinite(String storeName) {
        context.getStore(storeName).setKitchen(getInfiniteMockKitchen());
    }

    @Given("^A customer \"([^\"]*)\" choose a store \"([^\"]*)\" to pickup \"([^\"]*)\" in (\\d+) hours$")
    public void aCustomerChooseAStoreToPickupInHours(String name, String sStore, String sDay, int time) throws Throwable {

        context.getFacade().customerAddPickUpTimeAndStoreToOrder(utils.createEmail(name),sStore,time,sDay);
        
    }

    @Given("^A customer \"([^\"]*)\" add (\\d+) cookies from the \"([^\"]*)\"$")
    public void aCustomerAddCookiesFromThe(String name, int quantity, String sStore) throws Throwable {
        context.getFacade().customerAddCookies(utils.createEmail(name), sStore, quantity);
    }


    @When("^\"([^\"]*)\" place his order and pay \"([^\"]*)\"$")
    public void placeHisOrderAndPay(String name, String pay) throws Throwable {
        context.getFacade().customerPlaceOrder(utils.createEmail(name), utils.payOnline(pay));
    }
}
