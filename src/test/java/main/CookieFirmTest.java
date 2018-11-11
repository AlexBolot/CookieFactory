package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CookieFirmTest {

    private CookieFirm cookieFirm;
    private Guest guest = new Guest("email@email.fr");
    private Customer customer = new Customer(new ArrayList<Order>(),"","","","email@email.fr","");

    @Before
    public void before() {
        cookieFirm = new CookieFirm(new ArrayList<Store>(), new ArrayList<Manager>(),new ArrayList<Recipe>());
        cookieFirm.addGuest(guest);
    }

    @Test
    public void createAccount_newAccount() {
        Assert.assertTrue("CookieFirm incorrectly initialized",cookieFirm.getGuests().contains(guest));
        assertFalse("CookieFirm incorrectly initialized",cookieFirm.getAccounts().contains(customer));
        Customer createdAccount = cookieFirm.createAccount(new ArrayList<Order>(),"","",
                "","email@email.fr","");
        assertEquals("Customer object incorrectly created",createdAccount,customer);
        assertFalse("Linked Guest not removed",cookieFirm.getGuests().contains(guest));
        Assert.assertTrue("Customer not added",cookieFirm.getAccounts().contains(createdAccount));
    }

    @Test (expected = IllegalArgumentException.class)
    public void createAccount_existingAccount() {
        cookieFirm.createAccount(new ArrayList<Order>(),"a","a","a","email@email.fr","a");
        cookieFirm.createAccount(new ArrayList<Order>(),"b","b","b","email@email.fr","b");
    }
}