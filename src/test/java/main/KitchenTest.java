package main;

import ingredient.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class KitchenTest {

    private Catalog catalog = new Catalog();
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

        Kitchen kitchen = new Kitchen(stock);

        Recipe valid = new Recipe("", doughs.get(0), Arrays.asList(flavors.get(0), flavors.get(1)), Arrays.asList(toppings.get(0), toppings.get(1)), catalog.getMixList().get(0), catalog.getCookingList().get(0), 2);
        Recipe notContained = new Recipe("", doughs.get(0), Arrays.asList(flavors.get(2), flavors.get(1)), Collections.emptyList(), catalog.getMixList().get(0), catalog.getCookingList().get(0), 2);
        Recipe doubleFlavor = new Recipe("", doughs.get(0), Arrays.asList(flavors.get(1), flavors.get(1)), Collections.emptyList(), catalog.getMixList().get(0), catalog.getCookingList().get(0), 2);

        assertTrue(kitchen.canDo(valid));
        assertTrue(kitchen.canDo(doubleFlavor));
        assertFalse(kitchen.canDo(notContained));

        //Empty the flavors.get(1) stock
        stock.replace(flavors.get(1), 0);

        assertFalse(kitchen.canDo(valid));
    }

    @Test
    public void hasInStock() {
        Kitchen kitchen = new Kitchen(new HashMap<Ingredient, Integer>() {{
            put(flavors.get(0), 6);
        }});

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

        Kitchen kitchen = new Kitchen(stock);

        Recipe valid = new Recipe("", doughs.get(0), Collections.singletonList(flavors.get(0)), Arrays.asList(toppings.get(0), toppings.get(1)), catalog.getMixList().get(0), catalog.getCookingList().get(0), 2);

        kitchen.cook(valid, 3);

        assertEquals(7, (int) stock.get(flavors.get(0)));
    }

    @Test
    public void refill() {

        Flavor flavor = flavors.get(0);

        Map<Ingredient, Integer> stock = new HashMap<Ingredient, Integer>() {{
            put(flavor, 10);
        }};

        Kitchen kitchen = new Kitchen(stock);

        assertEquals(10, (int) stock.get(flavor));

        kitchen.refill(flavor, 10);

        assertEquals(20, (int) stock.get(flavor));

    }
}