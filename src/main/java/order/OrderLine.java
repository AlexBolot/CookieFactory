package order;

import recipe.Recipe;

import java.util.Objects;

public class OrderLine {
    private Recipe recipe;
    private int amount;

    OrderLine(Recipe recipe, int amount) {
        this.recipe = recipe;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    void reduceAmount(int amount) {
        this.amount-= amount;
    }

    /**
     * Set amount to a recipe
     * @param amount quantity of cookies
     */
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