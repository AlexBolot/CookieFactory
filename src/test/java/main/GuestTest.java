package main;

import order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GuestTest {

    private Order order;
    private Guest guest;

    @Before
    public void before() {
        order = new Order(new Store(14), LocalDateTime.now().plusHours(3), Day.MONDAY);
        guest = new Guest("");
        guest.setTemporaryOrder(order);
    }

    @Test
    public void placeOrder_OnlinePaiment() {
        assertFalse(order.isPayed());
        guest.placeOrder(true);
        Assert.assertTrue(order.isPayed());
    }

    @Test
    public void placeOrder_DeskPaiment() {
        assertFalse(order.isPayed());
        guest.placeOrder(false);
        assertFalse(order.isPayed());
        assertEquals(guest, order.getGuest());
    }

    @Test (expected = IllegalStateException.class)
    public void placeOrder_AlreadyPaid() {
        order.setPayed();
        guest.placeOrder(true);
    }
}