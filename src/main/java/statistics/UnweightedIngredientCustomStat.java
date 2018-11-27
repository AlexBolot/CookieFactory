package statistics;

import order.Order;
import order.OrderLine;
import recipe.Recipe;
import store.Store;

public class UnweightedIngredientCustomStat {
    private Store store;
    private IngredientRatio value;

    public UnweightedIngredientCustomStat(Store store) {
        this.store = store;
        this.value = new IngredientRatio();
    }

    public IngredientRatio computeValue() {
        Recipe recipe;
        for(Order order : store.getOrders()) {
            for(OrderLine orderline : order.getOrderLines()) {
                recipe = orderline.getRecipe();
                if(recipe.isCustom()) {
                    value.addDough(recipe.getDough(), 1);
                    value.addFlavor(recipe.getFlavor(), 1);
                    recipe.getToppings().forEach(topping -> value.addTopping(topping, 1));
                    value.addMix(recipe.getMix(), 1);
                    value.addCooking(recipe.getCooking(), 1);
                }
            }
        }
        this.value.normalizeAll();
        return value;
    }

    public IngredientRatio getValue() {
        return value;
    }
}
