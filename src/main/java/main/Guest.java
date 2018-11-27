package main;

import api.BankingData;
import order.Order;
import recipe.Recipe;
import recipe.RecipeBuilder;
import recipe.ingredient.*;

import java.util.List;


public class Guest {

    private Order temporaryOrder;
    private String email;
    private BankingData bankingData;

    public Guest(String email) {
        this.temporaryOrder = initOrder();
        this.email = email;
    }

    Order initOrder() {
        this.temporaryOrder = new Order();
        this.temporaryOrder.setGuest(this);
        return this.temporaryOrder;
    }

    public double placeOrder(boolean onlinePayment) {

        if (temporaryOrder.isPayed())
            throw new IllegalStateException("The order you are trying to place has already been paid");

        double price = temporaryOrder.getStore().placeOrder(temporaryOrder);

        if (onlinePayment) {
            temporaryOrder.setPayed();
        }

        setTemporaryOrder(initOrder());

        return price;
    }

    /**
     * Order a custom recipe by giving the ingrdients to a recipe Factory
     *
      * @param quantity of cookie ordered
     * @param dough
     * @param flavor
     * @param topping
     * @param mix
     * @param cooking
     * @return
     */
    public Recipe orderCustomRecipe(int quantity, Dough dough, Flavor flavor, List<Topping> topping, Mix mix, Cooking cooking) {
        RecipeBuilder rBuilder = new RecipeBuilder();
        Recipe customRecipe = rBuilder.createRecipe(dough, flavor, topping, mix, cooking);
        this.temporaryOrder.addCookie(customRecipe, quantity);
        return customRecipe;
    }

    protected void setTemporaryOrder(Order order) {
        this.temporaryOrder = order;
        this.temporaryOrder.setGuest(this);
    }

    public Order getTemporaryOrder() {
        return temporaryOrder;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BankingData getBankingData() {
        return bankingData;
    }

    public void setBankingData(BankingData bankingData) {
        this.bankingData = bankingData;
    }

    public boolean isInLoyaltyProgram () {return false;}

}