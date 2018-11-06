package order;

import main.Recipe;

import java.util.Objects;

public class OrderLine {
    public OrderLine(Recipe recipe, int amount) {
        this.recipe = recipe;
        this.amount = amount;
    }

    Recipe orderLines;
    Recipe recipe;
    int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount should be positive");
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return amount == orderLine.amount &&
                Objects.equals(orderLines, orderLine.orderLines) &&
                Objects.equals(recipe, orderLine.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderLines, recipe, amount);
    }
}