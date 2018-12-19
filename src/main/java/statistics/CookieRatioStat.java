package statistics;

import order.Order;
import order.OrderLine;
import store.Store;

import java.util.HashMap;
import java.util.Map;

public class CookieRatioStat extends Statistic<Map<String, Double>>{
    private Store store;

    public CookieRatioStat(Store store) {
        this.store = store;
    }

    public void calculate() {
        Map<String, Double> ratios = new HashMap<>();
        String recipe;
        for (Order order : store.getOrders()) {
            for (OrderLine orderline : order.getOrderLines()) {
                if (orderline.getRecipe().isCustom()) {
                    ratios.put("Custom", ratios.containsKey("Custom") ? ratios.get("Custom") + orderline.getAmount() : orderline.getAmount());
                } else {
                    recipe = orderline.getRecipe().getName();
                    ratios.put(recipe, ratios.containsKey(recipe) ? ratios.get(recipe) + orderline.getAmount() : orderline.getAmount());
                }
            }
        }
        value=ratios;
    }

    public void cleanUp() {
        double total=0;
        for (String r : value.keySet()) {
            total+=value.get(r);
        }
        for (String r : value.keySet()) {
            value.put(r, value.get(r) /  total);
        }
    }

}
