package StepDefinitions;

import api.UnFaithPassAPI;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import main.Guest;
import org.junit.Assert;
import recipe.Recipe;
import store.Reward;
import store.Store;
import store.UnFaithPassProgram;
import utils.CucumberContext;

import java.util.HashMap;
import java.util.Optional;

public class UnFaithPassStepdefs {

    private final CucumberContext context = CucumberContext.getContext();

    @Given("^The store \"([^\"]*)\" applies an UnFaithPassProgram$")
    public void theStoreAppliesAnUnFaithPassProgram(String storeName) throws Throwable {
        Optional<Store> opStore = context.cookieFirm().findStore(storeName);
        opStore.ifPresent(store -> store.applyUnFaithPathProgram(new UnFaithPassProgram(new HashMap<>())));
    }

    @Given("^The store \"([^\"]*)\" applies an UnFaithPass which gives (\\d+) point for the recipe \"([^\"]*)\"$")
    public void theStoreAppliesAnUnFaithPassWhichGivesPointForTheRecipe(String storeName, int point, String recipeName) throws Throwable {
        Optional<Store> opStore = context.cookieFirm().findStore(storeName);
        if(opStore.isPresent()) {
            for (Recipe recipe : context.cookieFirm().getGlobalRecipes()) {
                if (recipeName.equals(recipe.getName())) {
                    opStore.get().getUnFaithPassProgram().changeReward(recipe, new Reward(false, point));
                }
            }
        }
    }

    @Given("^The store \"([^\"]*)\" applies an UnFaithPass which gives (\\d+) free Cookies for the recipe \"([^\"]*)\"$")
    public void theStoreAppliesAnUnFaithPassWhichGivesFreeCookiesForTheRecipe(String storeName, int freeCookieNumber, String recipeName) throws Throwable {
        Optional<Store> opStore = context.cookieFirm().findStore(storeName);
        if (opStore.isPresent()) {
            for (Recipe recipe : context.cookieFirm().getGlobalRecipes()) {
                if (recipeName.equals(recipe.getName())) {
                    opStore.get().getUnFaithPassProgram().changeReward(recipe, new Reward(true, freeCookieNumber));
                }
            }
        }
    }

    @And("^The customer \"([^\"]*)\" has an empty UnFaithPass$")
    public void theCustomerHasAnEmptyUnFaithPass(String customerName) throws Throwable {
        Optional <Guest> opGuest = context.cookieFirm().findGuestOrCustomer(context.getCurrentId());
        opGuest.ifPresent(guest -> guest.setUnFaithPass(new UnFaithPassAPI(customerName, customerName)));
    }

    @And("^The customer \"([^\"]*)\" has (\\d+) point and (\\d+) free Cookies on his UnFaithPass$")
    public void theCustomerHasPointAndFreeCookiesOnHisUnFaithPass(String customerName, int points, int freeCookieNumber) throws Throwable {
        Optional <Guest> opGuest = context.cookieFirm().findGuestOrCustomer(context.getCurrentId());
        if(opGuest.isPresent()) {
            UnFaithPassAPI unFaithPass = opGuest.get().getUnFaithPass();
            Assert.assertEquals(points,unFaithPass.getPoints());
            Assert.assertEquals(freeCookieNumber,unFaithPass.getFreeCookies());
        }
    }
}
