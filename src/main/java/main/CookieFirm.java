package main;

import order.Order;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CookieFirm {

    private Set<Guest> guests;
    private Set<Customer> accounts;
    private List<Store> stores;
    private List<Manager> managers;
    private List<Recipe> globalRecipes;

    public CookieFirm(List<Store> stores, List<Manager> managers, List<Recipe> globalRecipes) {
        this.guests = new HashSet<>();
        this.accounts = new HashSet<>();
        this.stores = stores;
        this.managers = managers;
        this.globalRecipes = globalRecipes;
    }


    /**
     * @param recipe current recipe to check
     */
    private boolean checkRecipeExists(Recipe recipe) {
        return globalRecipes.contains(recipe);
    }

    Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password);
        if(saveCustomerAccountIfAbsent(newAccount, email)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }
    public Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password, Order temporaryOrder) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password, temporaryOrder);
        if(saveCustomerAccountIfAbsent(newAccount, email)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }

    private boolean saveCustomerAccountIfAbsent(Customer customer, String email) {
        for (Customer account : this.accounts) {
            if (account.getEmail().equals(email)) {
                return false;
            }
        }
        guests = guests.stream().filter(g -> !g.getEmail().equals(email)).collect(Collectors.toSet());
        accounts.add(customer);
        return true;
    }

    void addGuest(Guest guest) {
        guests.add(guest);
    }

    public List<Store> getStores() {
        return stores;
    }

    Set<Guest> getGuests() {
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