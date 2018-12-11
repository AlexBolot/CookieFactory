package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.sl.In;
import main.Guest;
import store.Manager;
import store.Store;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.getInfiniteMockKitchen;

public class CommonStepDefs {

    private final CucumberContext context = CucumberContext.getContext();
    private TestUtils utils = new TestUtils();

    @Given("^A store \"([^\"]*)\" with a tax (\\d+) and margin on recipe (\\d+)$")
    public void aStoreWithATaxAndMarginOnRecipe(String storeName, int tax, int recipeMargin) throws Throwable {
        Store store = context.getFacade().addAStoreToFirm(storeName, tax, recipeMargin);
        store.setMonthlyRecipe(utils.randomRecipe());
        store.setKitchen(getInfiniteMockKitchen());
    }
    @Given("^The kitchen for \"([^\"]*)\" is infinite$")
    public void theKitchenForIsInfinite(String storeName) {
        Optional<Store> opStore = context.cookieFirm().findStore(storeName);
        if(opStore.isPresent())
            opStore.get().setKitchen(getInfiniteMockKitchen());
    }

    @Given("^A customer \"([^\"]*)\"$")
    public void aCustomer(String name) {
        context.setCurrentId(context.getFacade().createACustomer(name, name, "07475674", utils.createEmail(name),
                name+"1234"));
        context.getFacade().setBankingDataCustomer(utils.createEmail(name), "02833777");
    }

    @Given("^A guest$")
    public void aGuest() {
        context.setCurrentId(context.getFacade().createGuest());
    }

    @Given("^\"([^\"]*)\" the manager of \"([^\"]*)\"$")
    public void theManagerOf(String managerName, String storeName) {
        context.getFacade().addManagerToStore(managerName, storeName);
    }

    @Given("^The store of \"([^\"]*)\" opens \"([^\"]*)\" (\\d+) hours ago and closes in (\\d+) hours$")
    public void theStoreOpensHoursAgoAndClosesInHours(String mManager, String dayName, int behindHours,
                                                      int aheadHours) {
        context.getFacade().addOpeningClosingTimeFromNow(mManager, dayName, behindHours, aheadHours);

    }


    @Given("^The customer choose a store \"([^\"]*)\" to pickup \"([^\"]*)\" in (\\d+) hours$")
    public void aCustomerChooseAStoreToPickupInHours(String sStore, String sDay, int time) throws Throwable {

        context.getFacade().guestAddPickUpTimeAndStoreToOrder(context.getCurrentId(),sStore,time,sDay);
        
    }

    @Given("^The customer add (\\d+) cookies from the \"([^\"]*)\"$")
    public void aCustomerAddCookiesFromThe(int quantity, String sStore) throws Throwable {
        context.getFacade().guestAddCookies(context.getCurrentId(), sStore, quantity);
    }


    @When("^The customer place his order and pay \"([^\"]*)\"$")
    public void placeHisOrderAndPay(String pay) throws Throwable {
        context.getFacade().guestPlaceOrder(context.getCurrentId(), utils.payOnline(pay));
    }


    @Then("^The \"([^\"]*)\" purchase the order with (\\d+), \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\" it$")
    public void thePurschaseTheOrderWithAndIt(String sStore, int time, String day, String email, String action) throws Throwable {
        context.getFacade().anEmployeeMakeAnActionOnOrder(sStore, time, day, email, action);
    }



    @And("^The order in the \"([^\"]*)\" with (\\d+), \"([^\"]*)\" made by \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theOrderInTheWithMadeByIs(String sStore, int time, String day, String email, String etat) throws Throwable {
        assertEquals(etat.toUpperCase(), context.getFacade().anEmployeeSearchAnOrderState(sStore, time, day, email));
    }

    @When("^The manager refill the stock of \"([^\"]*)\" \"([^\"]*)\" by (\\d+) in the kitchen of \"([^\"]*)\"$")
    public void theManagerRefillTheStockOfByInTheKitchenOf(String type, String ingredient, int quantity,
                                                           String store) throws Throwable {
        context.getFacade().addStockForTopping(store, type, ingredient, quantity);
    }
}
