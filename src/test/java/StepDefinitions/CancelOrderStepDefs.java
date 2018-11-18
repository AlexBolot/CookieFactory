package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.*;
import order.Order;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CancelOrderStepDefs {

    private Map<String, Store> stores = new HashMap<>();
    private Map<String, Customer> customers = new HashMap<>();
    private TestUtils utils = new TestUtils();
    private ArrayList<Recipe> globalRecipes = new ArrayList<>();

    private Collection<Order> ordersStore = new ArrayList<>();

    @Given("^A \"([^\"]*)\"$")
    public void a(String storeName) throws Throwable {
        HashMap<Day, LocalDateTime> openingTimes = new HashMap<>();
        HashMap<Day, LocalDateTime> closingTimes = new HashMap<>();
        Collection<Order> orders = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            globalRecipes.add(utils.randomRecipe());
        }

        for (Day day : Day.values()) {
            openingTimes.put(day, LocalDateTime.now().minusHours(5));
            closingTimes.put(day, LocalDateTime.now().plusHours(5));
        }

        Store store = new Store(utils.randomRecipe(), globalRecipes, orders, openingTimes, closingTimes, 15.5);
        stores.put(storeName, store);
    }

    @Given("^A \"([^\"]*)\" made an \"([^\"]*)\" into the \"([^\"]*)\"$")
    public void aMadeAnIntoThe(String sCustomer, String sOrder, String sStore) throws Throwable {
        Order order1 = new Order(stores.get(sStore),LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (3))), Day.TUESDAY );
        order1.addCookie(globalRecipes.get(1),10);
        Customer customer1 = new Customer("Jack","Jack","","jackjack","", order1);
        customers.put("Jack", customer1);

        customer1.placeOrder(true);
    }

    @And("^An employee see the \"([^\"]*)\"'s orders$")
    public void anEmployeeSeeTheSOrders(String sStore) throws Throwable {
        ordersStore =  stores.get(sStore).getOrders();
    }

    @When("^An employee cancel an \"([^\"]*)\" in the \"([^\"]*)\" with : \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void anEmployeeCancelAnInTheWith(String sOrder, String sStore, String sPickupTime, String sDay, String sEmail) throws Throwable {
        Optional<Order> order = stores.get(sStore).findOrder( LocalDateTime.of(LocalDate.now(), LocalTime.from(LocalDateTime.now().plusHours
                (Integer.parseInt(sPickupTime)))), Day.valueOf(sDay), sEmail);

        if(order.isPresent()){
            stores.get(sStore).cancelOrder(order.get());
        }
    }

    @Then("^The order is no longer in the list of orders of the \"([^\"]*)\"$")
    public void theOrderIsNoLongerInTheListOfOrdersOfThe(String sStore) throws Throwable {
        Assert.assertTrue(ordersStore.size()-1 == stores.get(sStore).getOrders().size());

    }

    @And("^The \"([^\"]*)\" receveid a \"([^\"]*)\"$")
    public void theReceveidA(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }



}
