package order;

import main.Recipe;

import java.util.Objects;

public class OrderLine {
    private Recipe recipe;
    private int amount;

    public OrderLine(Recipe recipe, int amount) {
        this.recipe = recipe;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void reduceAmount(int amount) {
        this.amount-= amount;
    }

    public void setAmount(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount should be positive");
        this.amount = amount;
    }

    public Recipe getRecipe() {
        return recipe;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return amount == orderLine.amount &&
                Objects.equals(recipe, orderLine.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, amount);
    }

}