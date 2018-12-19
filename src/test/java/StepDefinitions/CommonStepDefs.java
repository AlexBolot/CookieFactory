package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import store.Store;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static utils.TestUtils.getFixedClock;
import static utils.TestUtils.getInfiniteMockKitchen;

public class CommonStepDefs {

    private final CucumberContext context = CucumberContext.getContext();
    private final TestUtils utils = new TestUtils();

    @Given("^A store \"([^\"]*)\" with a tax (\\d+) and margin on recipe (\\d+)$")
    public void aStoreWithATaxAndMarginOnRecipe(String storeName, int tax, int recipeMargin) {
        Store store = context.getFacade().addAStoreToFirm(storeName, tax, recipeMargin);
        store.setMonthlyRecipe(utils.randomRecipe());
        store.setKitchen(getInfiniteMockKitchen());
    }

    @Given("^The kitchen for \"([^\"]*)\" is infinite$")
    public void theKitchenForIsInfinite(String storeName) {
        Optional<Store> opStore = context.cookieFirm().findStore(storeName);
        opStore.ifPresent(store -> store.setKitchen(getInfiniteMockKitchen()));
    }

    @Given("^A customer \"([^\"]*)\"$")
    public void aCustomer(String name) {
        context.setCurrentId(context.getFacade().createACustomer(name, name, "07475674", utils.createEmail(name), name + "1234"));
    }

    @Given("^A guest$")
    public void aGuest() {
        context.setCurrentId(context.getFacade().createGuest());
    }

    @Given("^\"([^\"]*)\" the manager of \"([^\"]*)\"$")
    public void theManagerOf(String managerName, String storeName) {
        context.getFacade().addManagerToStore(managerName, storeName);
    }

    @Given("^The store managed by \"([^\"]*)\" opens \"([^\"]*)\" at (\\d+):(\\d+) and closes at (\\d+):(\\d+)$")
    public void theStoreManagedByOpensAtAndClosesAt(String mName, String sDay,
                                                    int openingH, int openingM,
                                                    int closingH, int closingM) {
        context.getFacade().managerChangeOpeningClosingTime(mName, sDay, openingH, openingM, closingH, closingM);
    }


    @Given("^The customer add (\\d+) cookies \"([^\"]*)\" from the \"([^\"]*)\"$")
    public void theCustomerAddCookiesFromThe(int quantity, String recipeName, String sStore) {
        context.getFacade().guestAddOrRemoveCookie(context.getCurrentId(), sStore, quantity, recipeName, false);
    }

    @When("^The customer remove (\\d+) cookie \"([^\"]*)\" from the \"([^\"]*)\"$")
    public void theCustomerRemoveCookieFromThe(int quantity, String recipeName, String sStore) {
        context.getFacade().guestAddOrRemoveCookie(context.getCurrentId(), sStore, quantity, recipeName, true);
    }

    @Given("^The customer choose a store \"([^\"]*)\" to pickup \"([^\"]*)\" at (\\d+):(\\d+)$")
    public void theCustomerChooseAStoreToPickupAt(String storeName, String dayName, int hours, int minutes) {
        context.getFacade().guestAddPickUpTimeAndStoreToOrder(context.getCurrentId(), storeName, hours, minutes, dayName);
    }

    @When("^The customer place his order and pay \"([^\"]*)\"$")
    public void placeHisOrderAndPay(String pay) {
        try {
            context.getFacade().guestPlaceOrder(context.getCurrentId(), utils.payOnline(pay));
        } catch (Exception e) {
            context.pushException(e);
        }
    }

    @Then("^The \"([^\"]*)\" purchase the order with (\\d+):(\\d+), \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\" it$")
    public void thePurschaseTheOrderWithAndIt(String sStore, int hours, int minutes, String day, String email, String action) {
        context.getFacade().anEmployeeMakeAnActionOnOrder(sStore, hours, minutes, day, email, action);
    }

    @And("^The order in the \"([^\"]*)\" with (\\d+):(\\d+), \"([^\"]*)\" made by \"([^\"]*)\" is \"([^\"]*)\"$")
    public void theOrderInTheWithMadeByIs(String sStore, int hours, int minutes, String day, String email, String etat) {
        String actualState = context.getFacade().anEmployeeSearchAnOrderState(sStore, hours, minutes, day, email);
        assertTrue(etat.equalsIgnoreCase(actualState));
    }

    @When("^The manager refill the stock of \"([^\"]*)\" \"([^\"]*)\" by (\\d+) in the kitchen of \"([^\"]*)\"$")
    public void theManagerRefillTheStockOfByInTheKitchenOf(String type, String ingredient, int quantity, String store) {
        context.getFacade().anEmployeeAddsStockForTopping(store, type, ingredient, quantity);
    }

    @Given("^Now is \"([^\"]*)\" (\\d+):(\\d+)$")
    public void nowIs(String dayName, int hours, int minutes) {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayName.toUpperCase());

        context.cookieFirm().setClock(getFixedClock(dayOfWeek, hours, minutes));
    }

    @Given("^The customer set banking data with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void theCustomerSetBankingDataWithAnd(String name, String lastName, String accountNumber) throws Throwable {
        context.getFacade().guestAddBankingData(context.getCurrentId(), name, lastName, accountNumber);
    }
}
