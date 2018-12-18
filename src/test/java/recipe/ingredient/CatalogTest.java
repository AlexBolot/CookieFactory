package recipe.ingredient;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CatalogTest {
    private Catalog basicCatalog;

    @Before
    public void setUp() {
        basicCatalog = new Catalog();
        basicCatalog.getToppingList().add(new Topping("StayATop"));
        basicCatalog.getDoughList().add(new Dough("doughYouBest"));
        basicCatalog.getFlavorList().add(new Flavor("flavorite"));
    }


    // Ingredient Adding
    @Test
    public void addANewTopping() {
        String toppingName = "TopOfTheWorld";
        Topping topping = new Topping(toppingName);
        assertFalse(basicCatalog.getToppingList().contains(topping));
        assertEquals(topping, basicCatalog.addTopping(toppingName));
        assertTrue(basicCatalog.getToppingList().contains(topping));
    }

    @Test
    public void addToppingReturnAlreadyExistingNoAdding() {
        String toppingName = "StayATop";
        Topping topping = new Topping(toppingName);
        assertEquals(topping, basicCatalog.addTopping(toppingName));
        assertEquals(1, basicCatalog.getToppingList().stream().filter(topping::equals).count());
    }

    @Test
    public void addANewDough() {
        String doughName = "doughYouGetMe";
        Dough dough = new Dough(doughName);
        assertFalse(basicCatalog.getDoughList().contains(dough));
        assertEquals(dough, basicCatalog.addDough(doughName));
        assertTrue(basicCatalog.getDoughList().contains(dough));
    }

    @Test
    public void addDoughReturnAlreadyExistingNoAdding() {
        String DoughName = "doughYouBest";
        Dough Dough = new Dough(DoughName);
        assertEquals(Dough, basicCatalog.addDough(DoughName));
        assertEquals(1, basicCatalog.getDoughList().stream().filter(Dough::equals).count());
    }

    @Test
    public void addANewFlavor() {
        String flavorName = "FlavorOfTenesse";
        Flavor flavor = new Flavor(flavorName);
        assertFalse(basicCatalog.getFlavorList().contains(flavor));
        assertEquals(flavor, basicCatalog.addFlavor(flavorName));
        assertTrue(basicCatalog.getFlavorList().contains(flavor));
    }

    @Test
    public void addFlavorReturnAlreadyExistingNoAdding() {
        String FlavorName = "flavorite";
        Flavor Flavor = new Flavor(FlavorName);
        assertEquals(Flavor, basicCatalog.addFlavor(FlavorName));
        assertEquals(1, basicCatalog.getFlavorList().stream().filter(Flavor::equals).count());
    }

}
