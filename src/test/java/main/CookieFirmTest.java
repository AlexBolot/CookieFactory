package main;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

public class CookieFirmTest {

    private final CookieFirm cookieFirm = CookieFirm.instance();
    private final String guestEmail = new TestUtils().randomString();
    private final Guest guest = new Guest();
    private final Customer customer = new Customer("", "", "", "email@email.fr", "");
    private final TestUtils utils = new TestUtils();

    @Before
    public void before() {
        guest.setEmail(guestEmail);
        cookieFirm.addGuest(guest);
    }

    @After
    public void after() {
        cookieFirm.getGuests().clear();
        cookieFirm.getAccounts().clear();
    }

    @Test
    public void createAccount_newAccount() {
        Assert.assertTrue("CookieFirm incorrectly initialized", cookieFirm.getGuests().contains(guest));
        assertFalse("CookieFirm incorrectly initialized", cookieFirm.getAccounts().contains(customer));
        Customer createdAccount = cookieFirm.createAccount("", "", "", guestEmail, "");
        assertEquals("Customer object incorrectly created", createdAccount, customer);
        assertFalse("Linked Guest not removed", cookieFirm.getGuests().contains(guest));
        Assert.assertTrue("Customer not added", cookieFirm.getAccounts().contains(createdAccount));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccount_existingAccount() {
        cookieFirm.createAccount("a", "a", "a", "email@email.fr", "a");
        cookieFirm.createAccount("b", "b", "b", "email@email.fr", "b");
    }

    @Test
    public void createAccount_deletingOneAccount() {

        Guest guest2 = new Guest();
        guest2.setEmail("email2@email.fr");

        Guest guest3 = new Guest();
        guest3.setEmail("email3@email.fr");

        cookieFirm.addGuest(guest2);
        cookieFirm.addGuest(guest3);

        assertEquals("Guests incorrectly initialzed", 3, cookieFirm.getGuests().size());

        cookieFirm.createAccount("", "", "", guestEmail, "");

        assertEquals("Guest badly deleted", 2, cookieFirm.getGuests().size());
    }

    @Test
    public void recipesCreation() {
        Recipe trueSimpleRecipe = new Recipe("Cookie for nothing",
                cookieFirm.getCatalog().doughFromName("Chocolate"),
                null,
                new ArrayList<>(),
                cookieFirm.getCatalog().mixFromName("Topped"),
                cookieFirm.getCatalog().cookingFromName("Chewy"),
                false);
        Recipe falseSimpleRecipe = new Recipe("Cookie for nothing",
                cookieFirm.getCatalog().doughFromName("Chocolate"),
                null,
                new ArrayList<>(),
                cookieFirm.getCatalog().mixFromName("Topped"),
                cookieFirm.getCatalog().cookingFromName("Chewy"),
                true);
        Recipe trueComplexRecipe = new Recipe("White Dog",
                cookieFirm.getCatalog().doughFromName("Peanut Butter"),
                cookieFirm.getCatalog().flavorFromName("Vanilla"),
                Arrays.asList(cookieFirm.getCatalog().toppingFromName("White Chocolate"),
                        cookieFirm.getCatalog().toppingFromName("White Chocolate"),
                        cookieFirm.getCatalog().toppingFromName("White Chocolate")),
                cookieFirm.getCatalog().mixFromName("Mixed"),
                cookieFirm.getCatalog().cookingFromName("Crunchy"),
                false);
        Recipe falseComplexRecipe = new Recipe("White Doge",
                cookieFirm.getCatalog().doughFromName("Peanut Butter"),
                cookieFirm.getCatalog().flavorFromName("Vanilla"),
                Arrays.asList(cookieFirm.getCatalog().toppingFromName("White Chocolate"),
                        cookieFirm.getCatalog().toppingFromName("White Chocolate"),
                        cookieFirm.getCatalog().toppingFromName("White Chocolate")),
                cookieFirm.getCatalog().mixFromName("Mixed"),
                cookieFirm.getCatalog().cookingFromName("Crunchy"),
                false);

        Assert.assertTrue(cookieFirm.getGlobalRecipes().contains(trueSimpleRecipe));
        Assert.assertFalse(cookieFirm.getGlobalRecipes().contains(falseSimpleRecipe));
        Assert.assertTrue(cookieFirm.getGlobalRecipes().contains(trueComplexRecipe));
        Assert.assertFalse(cookieFirm.getGlobalRecipes().contains(falseComplexRecipe));
    }

    @Test
    public void findARecipeeByName() {
        Recipe expected = cookieFirm.getGlobalRecipes().get(1);
        Optional<Recipe> result = cookieFirm.findRecipee(expected.getName());
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void findARecipeCaseInsensitive() {
        Recipe expected = cookieFirm.getGlobalRecipes().get(1);
        Optional<Recipe> result = cookieFirm.findRecipee(expected.getName().toUpperCase());
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void findARecipeReturnsEmpty() {
        assertEquals(Optional.empty(), cookieFirm.findRecipee("Je Suis Improbable mais vrais"));
    }
}