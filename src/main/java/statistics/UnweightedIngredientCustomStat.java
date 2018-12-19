package statistics;

import order.Order;
import order.OrderLine;
import recipe.Recipe;
import store.Store;

public class UnweightedIngredientCustomStat extends Statistic<IngredientRatio> {
    private Store store;

    public UnweightedIngredientCustomStat(Store store) {
        this.store = store;
        this.value = new IngredientRatio();
    }

    public void calculate() {
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
    }

    public void cleanUp() {
        this.value.normalizeAll();
    }
}
