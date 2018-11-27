package statistics;

import order.Order;
import order.OrderLine;
import recipe.Recipe;
import store.Store;

public class WeightedIngredientCustomStat {
    private Store store;
    private IngredientRatio value;

    public WeightedIngredientCustomStat(Store store) {
        this.store = store;
        this.value = new IngredientRatio();
    }

    public IngredientRatio computeValue() {
        Recipe recipe;
        for(Order order : store.getOrders()) {
            for(OrderLine orderline : order.getOrderLines()) {
                recipe = orderline.getRecipe();
                if(recipe.isCustom()) {
                    value.addDough(recipe.getDough(), orderline.getAmount());
                    value.addFlavor(recipe.getFlavor(), orderline.getAmount());
                    recipe.getToppings().forEach(topping -> value.addTopping(topping, orderline.getAmount()));
                    value.addMix(recipe.getMix(), orderline.getAmount());
                    value.addCooking(recipe.getCooking(), orderline.getAmount());
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
