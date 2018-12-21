package StepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.Guest;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Store;
import utils.CucumberContext;
import utils.TestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CookieStatisticsStepDefs {
    private final TestUtils utils = new TestUtils();
    CucumberContext context = CucumberContext.getContext();

    @And("^The store \"([^\"]*)\" has the command set \"([^\"]*)\"$")
    public void theStoreHasTheCommandSet(String storeName, String setName) throws Throwable {
        List<Order> orders = new ArrayList<>();
        Optional<Store> opStore = context.cookieFirm().findStore(storeName);
        if (!opStore.isPresent())
            throw new IllegalArgumentException("Store not found");
        Store store = opStore.get();
        Catalog catalog = context.cookieFirm().getCatalog();
        switch (setName) {
            case "White Dog Favorite":
                Order order1 = new Order(store, LocalDateTime.of(2018, 12, 25, 12, 12));
                Order order2 = new Order(store, LocalDateTime.of(2018, 12, 25, 12, 12));
                order1.setGuest(new Guest());
                order2.setGuest(new Guest());
                order1.addCookie(utils.recipeFromName("White Dog"), 14);
                order1.addCookie(utils.recipeFromName("Cookie for nothing"), 3);
                order1.addCookie(new Recipe(
                        "Created recipe",
                        catalog.doughFromName("Plain"),
                        catalog.flavorFromName("Chili"),
                        new ArrayList<>(), catalog.mixFromName("Mixed"),
                        catalog.cookingFromName("Chewy"), true), 12);
                order2.addCookie(utils.recipeFromName("Cookie for nothing"), 1);
                order2.addCookie(new Recipe("My custom recipe",
                        catalog.doughFromName("Chocolate"),
                        catalog.flavorFromName("Cinnamon"),
                        Collections.singletonList(catalog.toppingFromName("Milk Chocolate")),
                        catalog.mixFromName("Topped"),
                        catalog.cookingFromName("Crunchy"),
                        true), 7);
                order2.addCookie(new Recipe("Recipe 2", catalog.doughFromName("Plain"),
                        catalog.flavorFromName("Vanilla"),
                        Collections.singletonList(catalog.toppingFromName("Reese's buttercup")),
                        catalog.mixFromName("Topped"),
                        catalog.cookingFromName("Crunchy"), true), 6);
                orders.add(order1);
                orders.add(order2);
                break;
            case "One cookie at multiple times":
                List<LocalTime> times = Arrays.asList(
                        LocalTime.of(16, 10),
                        LocalTime.of(16, 11),
                        LocalTime.of(16, 12),
                        LocalTime.of(16, 13),
                        LocalTime.of(6, 15),
                        LocalTime.of(6, 14),
                        LocalTime.of(6, 13),
                        LocalTime.of(23, 0),
                        LocalTime.of(23, 0),
                        LocalTime.of(10, 34),
                        LocalTime.of(10, 44),
                        LocalTime.of(10, 54),
                        LocalTime.of(10, 55),
                        LocalTime.of(14, 46),
                        LocalTime.of(14, 47),
                        LocalTime.of(14, 48)
                );
                LocalDate date = LocalDate.now();
                List<Order> ordersList = times.parallelStream().map(time -> new Order(store, LocalDateTime.of(date, time))).collect(Collectors.toList());
                ordersList.forEach(order -> order.addCookie(utils.recipeFromName("White Dog"), 1));
                orders.addAll(ordersList);
                break;
            default:
                orders = new ArrayList<>();
                break;
        }
        store.getOrders().clear();
        store.getOrders().addAll(orders);

    }

    @When("^\"([^\"]*)\" compute the cookie ratio$")
    public void computeTheCookieRatio(String managerName) throws Throwable {
        System.out.println(context.getFacade().managerQueriesCookieRatio(managerName));
    }

    @Then("^The cookie ratio seen by \"([^\"]*)\" is for the set \"([^\"]*)\"$")
    public void theCookieRatioSeenByIsForTheSet(String managerName, String setName) throws Throwable {
        String expected;
        switch (setName) {
            case ("White Dog Favorite"):
                expected = "{" +
                        '"' + "Cookie for nothing" + '"' +
                        ':' + '"' + (double) 4 / (double) 43 + '"' + ',' +
                        '"' + "Custom" + '"' +
                        ':' + '"' + (double) 25 / (double) 43 + '"' + ',' +
                        '"' + "White Dog" + '"' +
                        ':' + '"' + (double) 14 / (double) 43 + '"' +
                        "}";
                break;
            default:
                expected = "{}";
        }
        assertEquals(expected, context.getFacade().managerQueriesCookieRatio(managerName));
    }

    @When("^\"([^\"]*)\" compute the unweighted custom ingredient ratio$")
    public void computeTheUnwieightCustomIngredientRatio(String managerName) throws Throwable {
        context.getFacade().managerQueriesUnweightedCustomIngredientRatio(managerName);
    }


    @Then("^The unweighted custom ingredient ratio seen by \"([^\"]*)\" is for the set \"([^\"]*)\"$")
    public void theUnweightedCustomIngredientRatioSeenByIsForTheSet(String managerName, String setName) throws Throwable {
        String expected;
        switch (setName) {
            case ("White Dog Favorite"):
                // Chaine literal comming from the sample output, our goal is not to check it's validity but that it matches
                expected = "{\"dough\": " +
                        "{\"Plain\":\"0.6666666666666666\"," +
                        "\"Chocolate\":\"0.3333333333333333\"}," +
                        "\"flavor\": " +
                        "{\"Vanilla\":\"0.3333333333333333\"," +
                        "\"Cinnamon\":\"0.3333333333333333\"," +
                        "\"Chili\":\"0.3333333333333333\"}," +
                        "\"topping\": {\"Reese's buttercup\":\"0.5\",\"Milk Chocolate\":\"0.5\"}," +
                        "\"mix\": {\"Topped\":\"0.6666666666666666\",\"Mixed\":\"0.3333333333333333\"}," +
                        "\"cooking\": {\"Crunchy\":\"0.6666666666666666\",\"Chewy\":\"0.3333333333333333\"}}";
                break;
            default:
                expected = "{}";
        }
        String actual = context.getFacade().managerQueriesUnweightedCustomIngredientRatio(managerName);
        assertThat(actual, containsString("\"Plain\":\"0.6666666666666666\""));
        assertThat(actual, containsString("\"Vanilla\":\"0.3333333333333333\""));
        assertThat(actual, containsString("flavor"));
    }

    @When("^\"([^\"]*)\" compute the weighted custom ingredient ratio$")
    public void computeTheWeightedCustomIngredientRatio(String managerName) throws Throwable {
        context.getFacade().managerQueriesWeigthedCustomIngredientRatio(managerName);
    }

    @Then("^The weighted custom ingredient ratio seen by \"([^\"]*)\" is for the set \"([^\"]*)\"$")
    public void theWeightedCustomIngredientRatioSeenByIsForTheSet(String managerName, String setName) throws Throwable {
        String actual = context.getFacade().managerQueriesWeigthedCustomIngredientRatio(managerName);
        assertThat(actual, containsString("\"Plain\":\"0.72\""));
        assertThat(actual, containsString("\"Vanilla\":\"0.24\""));
        assertThat(actual, containsString("\"Reese's buttercup\":\"0.46153846153846156\""));
        assertThat(actual, containsString("\"Crunchy\":\"0.52\""));
    }

    @When("^\"([^\"]*)\" compute the pick up time count$")
    public void computeThePickUpTimeCount(String managerName) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println(context.getFacade().managerQueriesPickUpTimeCount(managerName));
    }

    @Then("^The compute pick up time count seend by \"([^\"]*)\" is for the set \"([^\"]*)\"$")
    public void theComputePickUpTimeCountSeendByIsForTheSet(String managerName, String setName) throws Throwable {
        String expected;
        switch (setName) {
            case "One cookie at multiple times":
                // Chaine literal comming from the sample output, our goal is not to check it's validity but that it matches
                expected = "{\"10:30\":4,\"06:00\":3,\"23:00\":2,\"16:00\":4,\"14:30\":3,}";
                break;
            default:
                expected = "{}";
        }
        assertEquals(expected, context.getFacade().managerQueriesPickUpTimeCount(managerName));
    }


}
