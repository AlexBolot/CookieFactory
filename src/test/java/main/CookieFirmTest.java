package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CookieFirmTest {

    private CookieFirm cookieFirm;
    private final Guest guest = new Guest("email@email.fr");
    private final Customer customer = new Customer("","","","email@email.fr","");
    private final TestUtils utils = new TestUtils();

    @Before
    public void before() {
        cookieFirm = new CookieFirm(new ArrayList<>(), new ArrayList<>());
        cookieFirm.addGuest(guest);
    }

    @Test
    public void createAccount_newAccount() {
        Assert.assertTrue("CookieFirm incorrectly initialized",cookieFirm.getGuests().contains(guest));
        assertFalse("CookieFirm incorrectly initialized",cookieFirm.getAccounts().contains(customer));
        Customer createdAccount = cookieFirm.createAccount("","",
                "","email@email.fr","");
        assertEquals("Customer object incorrectly created",createdAccount,customer);
        assertFalse("Linked Guest not removed",cookieFirm.getGuests().contains(guest));
        Assert.assertTrue("Customer not added",cookieFirm.getAccounts().contains(createdAccount));
    }

    @Test (expected = IllegalArgumentException.class)
    public void createAccount_existingAccount() {
        cookieFirm.createAccount("a","a","a","email@email.fr","a");
        cookieFirm.createAccount("b","b","b","email@email.fr","b");
    }

    @Test
    public void createAccount_deletingOneAccount() {
        cookieFirm.addGuest(new Guest("email2@email.fr"));
        cookieFirm.addGuest(new Guest("email3@email.fr"));
        assertEquals("Guests incorrectly initialzed",3,cookieFirm.getGuests().size());
        Customer createdAccount = cookieFirm.createAccount("","",
                "","email@email.fr","");
        assertEquals("Guest badly deleted",2,cookieFirm.getGuests().size());
    }
    
    @Test
    public void recipesCreation() {
        Recipe trueSimpleRecipe = new Recipe("Cookie for nothing",
                utils.doughFromName("Chocolate"),
                null,
                new ArrayList<>(),
                utils.mixFromName("Topped"),
                utils.cookingFromName("Chewy"),
                false);
        Recipe falseSimpleRecipe = new Recipe("Cookie for nothing",
                utils.doughFromName("Chocolate"),
                null,
                new ArrayList<>(),
                utils.mixFromName("Topped"),
                utils.cookingFromName("Chewy"),
                true);
        Recipe trueComplexRecipe = new Recipe("White Dog",
                utils.doughFromName("Peanut Butter"),
                utils.flavorFromName("Vanilla"),
                Arrays.asList(utils.toppingFromName("White Chocolate"),
                        utils.toppingFromName("White Chocolate"),
                        utils.toppingFromName("White Chocolate")),
                utils.mixFromName("Mixed"),
                utils.cookingFromName("Crunchy"),
                false);
        Recipe falseComplexRecipe = new Recipe("White Doge",
                utils.doughFromName("Peanut Butter"),
                utils.flavorFromName("Vanilla"),
                Arrays.asList(utils.toppingFromName("White Chocolate"),
                        utils.toppingFromName("White Chocolate"),
                        utils.toppingFromName("White Chocolate")),
                utils.mixFromName("Mixed"),
                utils.cookingFromName("Crunchy"),
                false);

        Assert.assertTrue(cookieFirm.getGlobalRecipes().contains(trueSimpleRecipe));
        Assert.assertFalse(cookieFirm.getGlobalRecipes().contains(falseSimpleRecipe));
        Assert.assertTrue(cookieFirm.getGlobalRecipes().contains(trueComplexRecipe));
        Assert.assertFalse(cookieFirm.getGlobalRecipes().contains(falseComplexRecipe));

    }
}