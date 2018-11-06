package order;

import main.Store;
import recipe.Recipe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Order {

    Store store;
    Collection<OrderLine> orderLines = new ArrayList<>();
    LocalDateTime pickUpTime;

    public Order(Store store, LocalDateTime pickUpTime) {
        this.store = store;
        this.pickUpTime = pickUpTime;
    }

    public double getPrice() {
        double storeTax = store.getTax();
        return orderLines.stream().mapToDouble(line -> line.amount * line.recipe.getPrice()).sum() * storeTax;
    }

    /**
     * @param recipe
     * @param amount
     */
    public void addCookie(Recipe recipe, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount should be a strictly positive number");
        Optional<OrderLine> orderLineOptional = orderLines.stream().filter(line -> line.recipe.equals(recipe)).findFirst();
        if (!orderLineOptional.isPresent()) {
            OrderLine orderLine = new OrderLine(recipe, amount);
            orderLines.add(orderLine);
        } else {
            OrderLine orderLine = orderLineOptional.get();
            orderLine.setAmount(orderLine.getAmount() + amount);
        }

    }

    /**
     * @param recipe
     * @param amount
     */
    public void removeCookie(Recipe recipe, int amount) {
        OrderLine orderLine = orderLines.stream()
                .filter(line -> line.recipe.equals(recipe))
                .findFirst().orElseThrow(IllegalArgumentException::new);
        orderLine.amount -= amount;
        if (orderLine.amount <= 0)
            orderLines.remove(orderLine);

    }

}