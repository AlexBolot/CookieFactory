package main;

import ingredient.*;
import order.Order;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CookieFirm {

    Set<Guest> guests;
    Set<Customer> accounts;
    List<Store> stores;
    List<Manager> managers;
    List<Recipe> globalRecipes;

    public CookieFirm(List<Store> stores, List<Manager> managers, List<Recipe> globalRecipes) {
        this.guests = new HashSet<Guest>();
        this.accounts = new HashSet<Customer>();
        this.stores = stores;
        this.managers = managers;
        this.globalRecipes = globalRecipes;
    }


    /**
     * @param cooking
     * @param mix
     * @param dough
     * @param flavors
     * @param toppings
     */
    public Recipe chooseMonthlyRecipe(String name, Cooking cooking, Mix mix, Dough dough, List<Flavor> flavors, List<Topping> toppings, float price, Store store) {
        Recipe newMonthlyRecipe = new Recipe(name, dough, flavors, toppings, mix, cooking, price);
        store.setMonthlyRecipe(newMonthlyRecipe);
        return newMonthlyRecipe;
    }

    /**
     * @param recipe
     */
    private boolean checkRecipeExists(Recipe recipe) {
        return globalRecipes.contains(recipe);
    }

    public Customer createAccount(Collection<Order> orderHistory, String firstName, String lastName, String phoneNumber, String email, String password) {
        Customer newAccount = new Customer(orderHistory, firstName, lastName, phoneNumber, email, password);
        if(updateAccounts(newAccount, email)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }
    public Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password, Order temporaryOrder) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password, temporaryOrder);
        if(updateAccounts(newAccount, email)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }

    public boolean updateAccounts(Customer customer, String email) {
        for (Customer account : this.accounts) {
            if (account.getEmail() == email) {
                return false;
            }
        }
        guests = guests.stream().filter(g -> g.getEmail() != email).collect(Collectors.toSet());
        accounts.add(customer);
        return true;
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    public List<Store> getStores() {
        return stores;
    }

    public Set<Guest> getGuests() {
        return guests;
    }

    public Set<Customer> getAccounts() {
        return accounts;
    }

    public List<Recipe> getGlobalRecipes() {
        return globalRecipes;
    }

    private void addCustomerToLoyaltyProgram(Customer customer){
        customer.addToLoyaltyProgram();
    }

}