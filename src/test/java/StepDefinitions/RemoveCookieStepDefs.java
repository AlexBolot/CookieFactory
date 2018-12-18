package StepDefinitions;

import cucumber.api.java.en.Then;
import main.Guest;
import order.OrderLine;
import utils.CucumberContext;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class RemoveCookieStepDefs {

    private final CucumberContext context= CucumberContext.getContext();

    @Then("^The order contains (\\d+) cookie recipee$")
    public void theOrderContainsCookieRecipee(int recipeeAmount) {
        Optional<Guest> guest = context.cookieFirm().findGuest(context.getCurrentId());
        if(guest.isPresent()){
            int count = 0;
            for(OrderLine orderLine : guest.get().getTemporaryOrder().getOrderLines()) {
                count += orderLine.getAmount();
            }
            assertEquals(count, recipeeAmount);
        }
    }
}
