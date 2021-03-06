package order;

import ingredient.Catalog;
import main.Day;
import main.Recipe;
import main.Store;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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

        Catalog catalog = new Catalog();

        Store store = new Store(1);
        recipe1 = new Recipe("real", catalog.getDoughList().get(1), new ArrayList<>(), new ArrayList<>(), catalog.getMixList().get(0), catalog.getCookingList().get(0), 1.2f);

        unavailableRecep = new Recipe("unreal", catalog.getDoughList().get(1), new ArrayList<>(), new ArrayList<>(), catalog.getMixList().get(0), catalog.getCookingList().get(0), 3.14f);
        LocalDateTime pickUpTime = LocalDateTime.now();
        order = new Order(store, pickUpTime, Day.TUESDAY);
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
        order.setStore(new Store(1.25));
        assertEquals(1.5, order.getPrice(), 0.0001);
        order.setStore(new Store(1));
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
}