package main;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public class Store {

    Recipe monthlyRecipe;
    Manager manager;
    Collection<Recipe> globalRecipes;
    Collection<Order> orders;

    public Recipe[] getRecipes() {
        // TODO - implement main.Store.getRecipes
        throw new UnsupportedOperationException();
    }

    public List<LocalTime> getAvailableHours() {
        // TODO - implement main.Store.getAvailableHours
        // TODO: BLAME ALEXANDRE
        throw new UnsupportedOperationException();
    }

    private float getLocalTax() {
        // TODO - implement main.Store.getLocalTax
        throw new UnsupportedOperationException();
    }

    public void transmitPaymentOrder() {
        // TODO - implement main.Store.transmitPaymentOrder
        throw new UnsupportedOperationException();
    }

    /**
     * @param monthlyRecipe
     */
    public void setMonthlyRecipe(Recipe monthlyRecipe) {
        this.monthlyRecipe = monthlyRecipe;
    }

    public boolean checkOrderDelay() {
        // TODO - implement main.Store.checkOrderDelay
        throw new UnsupportedOperationException();
    }

}