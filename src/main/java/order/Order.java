package order;

import main.Store;
import recipe.Recipe;

import java.time.LocalDateTime;
import java.util.Collection;

public class Order {

    Store store;
    Collection<OrderLine> orderLine;
    LocalDateTime pickUpTime;

    public void getPrice() {
        // TODO - implement order.Order.getPrice
        throw new UnsupportedOperationException();
    }

    /**
     * @param recipe
     * @param amount
     */
    public void addCookie(Recipe recipe, int amount) {
        // TODO - implement order.Order.addCookie
        throw new UnsupportedOperationException();
    }

    /**
     * @param recipe
     * @param amount
     */
    public void removeCookie(Recipe recipe, int amount) {
        // TODO - implement order.Order.removeCookie
        throw new UnsupportedOperationException();
    }

}