package statistics;

import order.Order;
import order.OrderLine;
import store.Store;

import java.util.HashMap;
import java.util.Map;

public class CookieRatioStat {
    private Store store;
    private Map<String, Double> value;

    public CookieRatioStat(Store store) {
        this.store = store;
    }

    public Map<String, Double> computeValue() {
        Map<String, Double> ratios = new HashMap<>();
        String recipe;
        int total = 0;
        for (Order order : store.getOrders()) {
            for (OrderLine orderline : order.getOrderLines()) {
                if (orderline.getRecipe().isCustom()) {
                    ratios.put("Custom", ratios.containsKey("Custom") ? ratios.get("Custom") + orderline.getAmount() : orderline.getAmount());
                } else {
                    recipe = orderline.getRecipe().getName();
                    ratios.put(recipe, ratios.containsKey(recipe) ? ratios.get(recipe) + orderline.getAmount() : orderline.getAmount());
                }
                    total += orderline.getAmount();
            }
        }
        for (String r : ratios.keySet()) {
            ratios.put(r, ratios.get(r) / ((double) total));
        }
        this.value = ratios;
        return ratios;
    }

    public Map<String, Double> getStat() {
        return this.value;
    }
}
