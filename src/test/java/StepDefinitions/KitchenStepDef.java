package StepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.Assert;
import recipe.ingredient.Ingredient;
import recipe.ingredient.Topping;
import store.Store;
import utils.CucumberContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KitchenStepDef {


    private final CucumberContext context = CucumberContext.getContext();

    @Then("^The kitchen of \"([^\"]*)\" has exactly or more than (\\d+) \"([^\"]*)\" \"([^\"]*)\"$")
    public void theKitchenOfHasExactly(String store, int quantity, String type, String ingredient) throws Throwable {

        Optional<Store> opStore = context.cookieFirm().findStore(store);

        if(opStore.isPresent())
        {
            if(type.equals("topping"))
                assert(opStore.get().getKitchen().hasInStock(context.getFacade().toppingFromName(ingredient),quantity));
            else if(type.equals("dough"))
                assert(opStore.get().getKitchen().hasInStock(context.getFacade().doughFromName(ingredient),quantity));
            else if(type.equals("flavor"))
                assert(opStore.get().getKitchen().hasInStock(context.getFacade().flavorFromName(ingredient),quantity));

        }
    }
}
