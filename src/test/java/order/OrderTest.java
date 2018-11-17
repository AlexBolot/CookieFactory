package order;

import ingredient.Catalog;
import main.Day;
import main.Guest;
import main.Recipe;
import main.Store;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static order.OrderState.*;
import static org.junit.Assert.*;

public class OrderTest {

    private Order order;
    private Recipe recipe1;
    private Recipe unavailableRecep;

    @Before
    public void setUp() {

        Catalog catalog = new Catalog();

        Store store = new Store(null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1);
        recipe1 = new Recipe("real", catalog.getDoughList().get(1), new ArrayList<>(), new ArrayList<>(), catalog.getMixList().get(0), catalog.getCookingList().get(0), 1.2f);

        unavailableRecep = new Recipe("unreal", catalog.getDoughList().get(1), new ArrayList<>(), new ArrayList<>(), catalog.getMixList().get(0), catalog.getCookingList().get(0), 3.14f);
        LocalDateTime pickUpTime = LocalDateTime.now();
        order = new Order(store, pickUpTime, Day.TUESDAY);

        Guest guest = new Guest("Bob");
        order.setGuest(guest);
    }

    @Test
    public void basicGetPrices() {

        order.addCookie(recipe1, 2);
        assertEquals(2.4, order.getPrice(), 0.0001);
    }

    @Test
    public void emptyShouldBeFree() {
        assertEquals(0.0, order.getPrice(), 0.0);
    }

    @Test
    public void pricesUseStoreTax() {
        order.addCookie(recipe1, 1);
        order.setStore(new Store(null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1.25));
        assertEquals(1.5, order.getPrice(), 0.0001);
        order.setStore(new Store(null, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1));
        assertEquals(1.2, order.getPrice(), 0.0001);

    }

    @Test
    public void basicAddCookie() {
        assertEquals(0, order.getOrderLines().size());
        order.addCookie(recipe1, 3);
        assertEquals(1, order.getOrderLines().size());
        assertTrue(order.getOrderLines().contains(new OrderLine(recipe1, 3)));

    }

    @Test
    public void addAlreadyExistingCookieMergeLines() {
        assertEquals(0, order.getOrderLines().size());
        order.addCookie(recipe1, 3);
        assertEquals(1, order.getOrderLines().size());
        assertTrue(order.getOrderLines().contains(new OrderLine(recipe1, 3)));

        order.addCookie(recipe1, 2);
        assertEquals(1, order.getOrderLines().size());
        assertTrue(order.getOrderLines().contains(new OrderLine(recipe1, 5)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void addingNegativeThrows() {
        order.addCookie(unavailableRecep, -1);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void addUnavailableCookieThrowsAndException() {
        order.addCookie(unavailableRecep, 1);
    }

    @Test
    public void basicRemoveCookie() {
        order.addCookie(recipe1, 3);
        order.removeCookie(recipe1, 1);
        assertEquals(1, order.getOrderLines().size());
        assertTrue(order.getOrderLines().contains(new OrderLine(recipe1, 2)));
    }

    @Test
    public void removeAllCookieDeletesLine() {
        order.addCookie(recipe1, 3);
        assertEquals(1, order.getOrderLines().size());
        order.removeCookie(recipe1, 2);
        order.removeCookie(recipe1, 1);
        assertEquals(0, order.getOrderLines().size());

    }

    @Test(expected = IllegalArgumentException.class)
    public void removeAbsentCookiesThrows() {
        order.removeCookie(recipe1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNonExistingCookiesThrows() {
        order.removeCookie(unavailableRecep, 3);
    }

    @Test
    public void withdrawPayedOrder() {

        order.placeOrder();
        order.pay();

        // No exception should thow
        order.withdraw();

        assertEquals(WITHDRAWN, order.getState());
    }

    @Test (expected = WithdrawNotPaidOrderException.class)
    public void withdrawNotPayedOrder() {

        order.placeOrder();

        // Should throw exception
        order.withdraw();
    }

    @Test (expected = IllegalStateException.class)
    public void withdrawNotOrdered() {

        // Should throw exception
        order.withdraw();
    }

    @Test
    public void placeOrderDraftOrder() {

        assertEquals(DRAFT, order.getState());

        order.placeOrder();

        assertEquals(ORDERED, order.getState());
    }

    @Test (expected = IllegalStateException.class)
    public void placeOrderNotDraft() {

        assertEquals(DRAFT, order.getState());

        order.placeOrder();

        //Should throw exception
        order.placeOrder();
    }

    @Test
    public void cancelOrdered() {
        order.placeOrder();

        order.cancel();

        assertEquals(CANCELED, order.getState());
    }

    @Test (expected = IllegalStateException.class)
    public void cancelWithdrawn() {
        order.placeOrder();
        order.pay();
        order.withdraw();

        //Should throw exception
        order.cancel();
    }

    @Test (expected = IllegalStateException.class)
    public void cancelCanceled() {
        order.placeOrder();
        order.cancel();

        //Should throw exception
        order.cancel();
    }

}