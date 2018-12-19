package main;

import api.BankingData;
import api.UnFaithPassAPI;
import order.Order;
import recipe.Recipe;
import recipe.RecipeBuilder;
import recipe.ingredient.*;

import java.util.List;
import java.util.Objects;

public class Guest {

    private int id;
    private Order temporaryOrder;
    private String email;
    private BankingData bankingData;
    private UnFaithPassAPI unFaithPass;

    private static int IdCount = 0;

    public Guest() {
        this.temporaryOrder = initOrder();
        this.id = IdCount++;
    }

    /**
     * Create a new order an put it in temporaryOrder
     * @return the created order
     */
    Order initOrder() {
        this.temporaryOrder = new Order();
        this.temporaryOrder.setGuest(this);
        return this.temporaryOrder;
    }

    /**
     * Place the order in the de correct store
     * Reinitialise the temporary order
     * @param onlinePayment boolean that indicates if the order is payed online or not
     * @return the price of the order
     */
    public double placeOrder(boolean onlinePayment) {
        if (onlinePayment) {
            temporaryOrder.setBankingData(bankingData);
        }

        double price = temporaryOrder.getStore().placeOrder(temporaryOrder);

        setTemporaryOrder(initOrder());

        return price;
    }

    /**
     * Order a custom recipe by giving the ingredients to a recipe Factory
     * @param quantity of cookie ordered
     * @param dough of the custom cookie
     * @param flavor of the custom cookie
     * @param topping custom cookie's list of topping
     * @param mix of the custom cookie
     * @param cooking of the custom cookie
     */
    void orderCustomRecipe(int quantity, Dough dough, Flavor flavor, List<Topping> topping, Mix mix, Cooking cooking) {
        RecipeBuilder rBuilder = new RecipeBuilder();
        Recipe customRecipe = rBuilder.createRecipe(dough, flavor, topping, mix, cooking);
        this.temporaryOrder.addCookie(customRecipe, quantity);
    }

    void setTemporaryOrder(Order order) {
        this.temporaryOrder = order;
        this.temporaryOrder.setGuest(this);
    }

    public Order getTemporaryOrder() {
        return temporaryOrder;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
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

    public UnFaithPassAPI getUnFaithPass() {return unFaithPass;}

    public void setUnFaithPass (UnFaithPassAPI unFaithPassAPI) {this.unFaithPass = unFaithPassAPI;}

    public boolean isInLoyaltyProgram() {
        return false;
    }

    public boolean canHaveDiscount() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guest)) return false;
        Guest guest = (Guest) o;
        return id == guest.id &&
                Objects.equals(temporaryOrder, guest.temporaryOrder) &&
                Objects.equals(email, guest.email) &&
                Objects.equals(bankingData, guest.bankingData);
    }
}