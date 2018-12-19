package statistics;

import order.Order;
import store.Store;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compute the average order withdrawal time, to the half hour floored.
 */
public class PickUpTimeCountStat extends Statistic<Map<LocalTime, Integer>> {
    Store store;

    public PickUpTimeCountStat(Store store) {
        this.store = store;
        this.value = new HashMap<>();
    }


    @Override
    void calculate() {
        Collection<Order> orders = this.store.getOrders();
        //Maping order to time equivalend and collecting the common timing in a map
        Map<LocalTime, List<LocalTime>> grouped =
                orders.parallelStream().map(this::orderToRoundedTime).collect(Collectors.groupingBy(l -> l));
        grouped.forEach((entry, list) -> value.put(entry, list.size()));
    }

    /**
     * Take and order and return it's matching time rounded down to the nearest half hour
     *
     * @param order {@link Order} source order
     * @return {@link LocalTime} rounded time
     */
    private LocalTime orderToRoundedTime(Order order) {
        LocalTime localTime = order.getPickUpTime().toLocalTime();
        localTime = localTime.minusMinutes(localTime.getMinute() % 30);
        return localTime;
    }

    @Override
    void cleanUp() {
        //No cleanup needed for this stat.
    }

    @Override
    String serialize() {

        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<LocalTime, Integer> entry : value.entrySet()) {
            LocalTime key = entry.getKey();
            Integer ammount = entry.getValue();
            sb.append(key);
            sb.append(":");
            sb.append(ammount);
            sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }
}
