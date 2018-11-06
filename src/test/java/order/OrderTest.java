package order;

import main.Store;
import org.junit.Before;
import org.junit.Test;
import recipe.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderTest {

    private Order order;
    private Recipe recipe1;
    private Recipe unavailableRecep;

    @Before
    public void setUp() throws Exception {
        Store store = new Store(1);
        recipe1 = new Recipe(new Dough(), new ArrayList<Flavor>(), new ArrayList<Topping>(), new Mix(), new Cooking(), 1.2f);

        unavailableRecep = new Recipe(new Dough(), new ArrayList<Flavor>(), new ArrayList<Topping>(), new Mix(), new Cooking(), 3.14f);
        LocalDateTime pickUpTime = LocalDateTime.now();
        order = new Order(store, pickUpTime);
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
        order.store = new Store(1.25);
        assertEquals(1.5, order.getPrice(), 0.0001);
        order.store = new Store(1);
        assertEquals(1.2, order.getPrice(), 0.0001);

    }

    @Test
    public void basicAddCookie() {
        assertEquals(0, order.orderLines.size());
        order.addCookie(recipe1, 3);
        assertEquals(1, order.orderLines.size());
        assertTrue(order.orderLines.contains(new OrderLine(recipe1, 3)));

    }

    @Test
    public void addAlreadyExistingCookieMergeLines() {
        assertEquals(0, order.orderLines.size());
        order.addCookie(recipe1, 3);
        assertEquals(1, order.orderLines.size());
        assertTrue(order.orderLines.contains(new OrderLine(recipe1, 3)));

        order.addCookie(recipe1, 2);
        assertEquals(1, order.orderLines.size());
        assertTrue(order.orderLines.contains(new OrderLine(recipe1, 5)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void addingNegativeThrows() {
        order.addCookie(unavailableRecep, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUnavailableCookieThrowsAndException() {
        order.addCookie(unavailableRecep, 1);
    }

    @Test
    public void basicRemoveCookie() {
        order.addCookie(recipe1, 3);
        order.removeCookie(recipe1, 1);
        assertEquals(1, order.orderLines.size());
        assertTrue(order.orderLines.contains(new OrderLine(recipe1, 2)));
    }

    @Test
    public void removeAllCookieDeletesLine() {
        order.addCookie(recipe1, 3);
        assertEquals(1, order.orderLines.size());
        order.removeCookie(recipe1, 2);
        order.removeCookie(recipe1, 1);
        assertEquals(0, order.orderLines.size());

    }

    @Test(expected = IllegalArgumentException.class)
    public void removeAbsentCookiesThrows() {
        order.removeCookie(recipe1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNonExistingCookiesThrows() {
        order.removeCookie(unavailableRecep, 3);
    }
}