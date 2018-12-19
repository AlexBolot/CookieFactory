package StepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import store.Store;
import store.UnFaithPassProgram;
import utils.CucumberContext;
import utils.TestUtils;

import java.util.HashMap;
import java.util.Optional;

import static utils.TestUtils.getInfiniteMockKitchen;

public class ChangeUnFaithPassRatioStepdefs {

    private final CucumberContext context = CucumberContext.getContext();
    private final TestUtils utils = new TestUtils();

    @Given("^A store \"([^\"]*)\" with an UnFaithPass ratio of (\\d+)$")
    public void aStoreWithAnUnFaithPassRatioOf(String storeName, double ratio) {
        Store store = context.getFacade().addAStoreToFirm(storeName, 10, 1);
        store.setMonthlyRecipe(utils.randomRecipe());
        store.setKitchen(getInfiniteMockKitchen());
        store.applyUnFaithPathProgram(new UnFaithPassProgram(new HashMap<>(), ratio));

    }

    @When("^\"([^\"]*)\" changes the UnFaithPass ratio to (\\d+)$")
    public void changesTheUnFaithPassRatioTo(String managerName, int ratio) {
        context.getFacade().managerChangeUnFaithPassRewardRatio(managerName, ratio);
    }

    @Then("^Converting (\\d+) reward-value points in \"([^\"]*)\" gives (\\d+) units of cash$")
    public void convertingRewardValuePointsGivesUnitsOfCash(int rewardValuePoints, String storeName, int expectedCash) {
        Optional<Store> store = context.cookieFirm().findStore(storeName);
        if(store.isPresent()) {
            double actualCash = store.get().getUnFaithPassProgram().getCashFromRewardValue(rewardValuePoints);

            Assert.assertEquals(expectedCash, actualCash, 0.0001);
        }
    }
}
