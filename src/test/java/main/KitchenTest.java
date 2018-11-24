package main;

import org.junit.Before;
import org.junit.Test;
import recipe.Recipe;
import recipe.ingredient.*;
import store.Kitchen;
import utils.TestUtils;

import java.util.*;

import static org.junit.Assert.*;
import static utils.TestUtils.fillKitchenForRecipe;

public class KitchenTest {

    private final TestUtils utils = new TestUtils();
    private final Catalog catalog = new Catalog();
    private List<Flavor> flavors;
    private List<Topping> toppings;
    private List<Dough> doughs;

    @Before
    public void setUp() {
        flavors = catalog.getFlavorList();
        toppings = catalog.getToppingList();
        doughs = catalog.getDoughList();
    }

    @Test
    public void canDo() {

        Map<Ingredient, Integer> stock = new HashMap<Ingredient, Integer>() {{
            put(flavors.get(0), 10);
            put(flavors.get(1), 10);
            put(toppings.get(0), 10);
            put(toppings.get(1), 10);
            put(doughs.get(0), 10);
        }};

        Kitchen kitchen = new Kitchen(stock, new HashMap<>(), new HashMap<>());

        Recipe valid = new Recipe("", doughs.get(0), flavors.get(0), Arrays.asList(toppings.get(0), toppings.get(1)), catalog.getMixList().get(0), catalog.getCookingList().get(0), true);
        Recipe notContained = new Recipe("", doughs.get(0), flavors.get(0), Arrays.asList(toppings.get(2), toppings.get(1)), catalog.getMixList().get(0), catalog.getCookingList().get(0), true);
        Recipe doubleTopping = new Recipe("", doughs.get(0), flavors.get(1), Arrays.asList(toppings.get(1), toppings.get(1)), catalog.getMixList().get(0), catalog.getCookingList().get(0), true);

        assertTrue(kitchen.canDo(valid, 1));
        assertTrue(kitchen.canDo(doubleTopping, 1));
        assertFalse(kitchen.canDo(notContained, 1));

        //Empty the flavors.get(1) stock
        stock.replace(toppings.get(1), 0);

        assertFalse(kitchen.canDo(valid, 1));
    }

    @Test
    public void hasInStock() {
        Kitchen kitchen = new Kitchen(new HashMap<Ingredient, Integer>() {{
            put(flavors.get(0), 6);
        }}, new HashMap<>(), new HashMap<>());

        assertTrue(kitchen.hasInStock(flavors.get(0), 5));
        assertFalse(kitchen.hasInStock(flavors.get(0), 10));
        assertFalse(kitchen.hasInStock(flavors.get(1), 1));
    }

    @Test
    public void cook() {

        Map<Ingredient, Integer> stock = new HashMap<Ingredient, Integer>() {{
            put(flavors.get(0), 10);
            put(toppings.get(0), 10);
            put(toppings.get(1), 10);
            put(doughs.get(0), 10);
        }};

        Kitchen kitchen = new Kitchen(stock, new HashMap<>(), new HashMap<>());

        Recipe valid = new Recipe("", doughs.get(0), flavors.get(0), Arrays.asList(toppings.get(0), toppings.get(0)), catalog.getMixList().get(0), catalog.getCookingList().get(0), true);

        kitchen.cook(valid, 3);

        assertEquals(4, (int) stock.get(toppings.get(0)));
        assertEquals(7, (int) stock.get(flavors.get(0)));
    }

    @Test
    public void refill() {

        Flavor flavor = flavors.get(0);

        Map<Ingredient, Integer> stock = new HashMap<Ingredient, Integer>() {{
            put(flavor, 10);
        }};

        Kitchen kitchen = new Kitchen(stock, new HashMap<>(), new HashMap<>());

        assertEquals(10, (int) stock.get(flavor));

        kitchen.refill(flavor, 10);

        assertEquals(20, (int) stock.get(flavor));

    }

    @Test
    public void recipeCapacity() {
        Kitchen kitchen = new Kitchen();
        CookieFirm firm = new CookieFirm(Collections.emptyList(), Collections.emptyList());
        Recipe recipe = firm.getGlobalRecipes().get(0);
        assertEquals(0, kitchen.recipeCapacity(recipe));
        fillKitchenForRecipe(kitchen, recipe, 2);
        assertEquals(2, kitchen.recipeCapacity(recipe));
    }

    @Test
    public void vendingPriceOf() {
        Kitchen kitchen = new Kitchen();

        Ingredient ingredient = utils.flavorFromName("Vanilla");

        kitchen.setMarginOf(ingredient, 10);
        kitchen.setSupplierPriceOf(ingredient, 2);

        double expectedPrice = 2.2;
        double actualPrice = kitchen.vendingPriceOf(ingredient);

        assertEquals(expectedPrice, actualPrice, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void vendingPriceOf_UnkownMargin() {
        Kitchen kitchen = new Kitchen();

        Ingredient ingredient = utils.flavorFromName("Vanilla");

        kitchen.setSupplierPriceOf(ingredient, 2);

        kitchen.vendingPriceOf(ingredient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void vendingPriceOf_UnkownSupplierPrice() {
        Kitchen kitchen = new Kitchen();

        Ingredient ingredient = utils.flavorFromName("Vanilla");

        kitchen.setMarginOf(ingredient, 2);

        kitchen.vendingPriceOf(ingredient);
    }
}