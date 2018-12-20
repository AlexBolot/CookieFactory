package order;

import api.BankingData;
import main.CookieFirm;
import main.Guest;
import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Kitchen;
import store.Manager;
import store.Store;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static jdk.nashorn.internal.objects.NativeMath.max;
import static order.OrderState.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static utils.TestUtils.*;

public class OrderTest {

    private Order order;
    private Recipe recipe1;
    private Recipe unavailableRecep;
    private CookieFirm cookieFirm;
    private Guest guest;

    private final LocalDateTime testingTime = LocalDateTime.now().withHour(13).withMinute(20);

    @Before
    public void setUp() {
        Catalog catalog = new Catalog();

        Store store = new Store("", null, new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1, 1);
        store.setKitchen(new Kitchen());

        recipe1 = new Recipe(
                "real",
                catalog.getDoughList().get(1),
                catalog.getFlavorList().get(0),
                new ArrayList<>(),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                true);

        cookieFirm = CookieFirm.instance();
        cookieFirm.setClock(getFixedClock(testingTime.getHour(), testingTime.getMinute()));
        unavailableRecep = new Recipe("unreal", catalog.getDoughList().get(1), catalog.getFlavorList().get(0), new ArrayList<>(), catalog.getMixList().get(0), catalog.getCookingList().get(0), true);
        LocalDateTime pickUpTime = testingTime.plusHours(3);

        Manager manager = new Manager(store, "bob");

        manager.changeOpeningTime(pickUpTime.getDayOfWeek(), LocalTime.of(8, 0));
        manager.changeClosingTime(pickUpTime.getDayOfWeek(), LocalTime.of(19, 0));

        guest = new Guest();
        guest.setBankingData(new BankingData("FirstName", "LastName", "04838229405"));
        guest.getTemporaryOrder().setStore(store);
        guest.getTemporaryOrder().setPickUpTime(pickUpTime);

        order = guest.getTemporaryOrder();
        order.setGuest(guest);

        fillKitchenForRecipe(store.getKitchen(), recipe1, 50);

        cookieFirm.setBankAPI(lenientBankAPI());
    }

    @Test
    public void basicGetPrices() {
        order.addCookie(recipe1, 2);
        order.getStore().setKitchen(getInfiniteMockKitchen());
        Kitchen kitchen = order.getStore().getKitchen();
        when(kitchen.vendingPriceOf(any())).thenReturn(1.0);

        assertEquals(6, order.getPrice(), 0.0001);
    }

    @Test
    public void emptyShouldBeFree() {
        assertEquals(0.0, order.getPrice(), 0.0);
    }


    @Test
    public void pricesUseStoreTax() {
        order.addCookie(recipe1, 1);
        Store store = new Store("", null, new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1.25, 1.0);
        Kitchen mockKitchen = getInfiniteMockKitchen();
        store.setKitchen(mockKitchen);
        when(mockKitchen.vendingPriceOf(any())).thenReturn(1.0);
        order.setStore(store);

        assertEquals(3.75, order.getPrice(), 0.0001);
        Store store2 = new Store("", null, new ArrayList<>(), new HashMap<>(), new HashMap<>(), 1, 1);
        order.setStore(store2);
        store2.setKitchen(mockKitchen);
        assertEquals(3.0, order.getPrice(), 0.0001);

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

    @Test
    public void notEnoughIngredientOnAddReturnFalse() {
        order.getStore().setKitchen(new Kitchen());
        assertFalse(order.addCookie(cookieFirm.getGlobalRecipes().get(0), 1));
    }

    @Test
    public void notEnoughForAmountTruncate() {
        Kitchen kitchen = new Kitchen();
        Recipe recipe = cookieFirm.getGlobalRecipes().get(0);
        int recipeCount = 2;
        kitchen.refill(recipe.getDough(), recipeCount);
        kitchen.refill(recipe.getFlavor(), recipeCount);
        recipe.getToppings().forEach(t -> kitchen.refill(t, recipeCount));
        order.getStore().setKitchen(kitchen);

        assertTrue(order.addCookie(recipe, 3));
        assertEquals(2, order.getOrderLines().get(0).getAmount());
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
    public void withdrawOrderWithBankingData() {
        cookieFirm.setBankAPI(lenientBankAPI());
        order.addCookie(recipe1, 5);
        guest.placeOrder(true);

        // No exception should throw
        order.withdraw();

        assertEquals(WITHDRAWN, order.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawOrderWithoutBankingData() {
        cookieFirm.setBankAPI(lenientBankAPI());
        order.addCookie(recipe1, 5);
        guest.placeOrder(true);

        // No exception should throw
        order.withdraw();

        // Should throw exception
        order.withdraw();
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawNotOrdered() {

        // Should throw exception
        order.withdraw();
    }

    @Test
    public void withdrawOrderWithValidDiscount() {
        cookieFirm.setBankAPI(lenientBankAPI());

        order.addCookie(recipe1, 5);
        guest.placeOrder(true);

        double defaultPrice = order.getPrice();

        // No exception should throw
        order.withdraw(max(defaultPrice - 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdrawOrderWithNegativeDiscount() {
        cookieFirm.setBankAPI(lenientBankAPI());
        guest.placeOrder(true);

        // No exception should throw
        order.withdraw(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdrawOrderWithTooBigDiscount() {
        cookieFirm.setBankAPI(lenientBankAPI());
        guest.placeOrder(true);

        double defaultPrice = order.getPrice();

        // No exception should throw
        order.withdraw(defaultPrice + 1);
    }

    @Test
    public void placeOrderDraftOrder() {

        assertEquals(DRAFT, order.getState());

        order.placeOrder();

        assertEquals(ORDERED, order.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void placeOrderNotDraft() {

        assertEquals(DRAFT, order.getState());

        order.placeOrder();

        //Should throw exception
        order.placeOrder();
    }

    @Test
    public void cancelOrdered() {
        cookieFirm.setBankAPI(lenientBankAPI());
        order.placeOrder();

        order.cancel();

        assertEquals(CANCELED, order.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void cancelWithdrawn() {
        order.placeOrder();
        order.withdraw();

        //Should throw exception
        order.cancel();
    }

    @Test(expected = IllegalStateException.class)
    public void cancelCanceled() {
        cookieFirm.setBankAPI(lenientBankAPI());
        order.placeOrder();
        order.cancel();

        //Should throw exception
        order.cancel();
    }

}