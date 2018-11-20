package main;

import order.Order;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Manager;
import store.Store;

import java.util.*;
import java.util.stream.Collectors;

public class CookieFirm {

    private Set<Guest> guests;
    private Set<Customer> accounts;
    private List<Store> stores;
    private List<Manager> managers;
    private List<Recipe> globalRecipes;

    public CookieFirm(List<Store> stores, List<Manager> managers) {
        this.guests = new HashSet<>();
        this.accounts = new HashSet<>();
        this.stores = stores;
        this.managers = managers;
        this.globalRecipes = new ArrayList<>();

        Catalog catalog = new Catalog();

        //Recipe 1: Peanut Butter / Chili / / Mixed / Crunchy
        this.globalRecipes.add(new Recipe("Pathway to hell",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(2),
                new ArrayList<>(),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                1.4f));

        //Recipe 2: Oatmeal / Vanilla / White Chocolate / Mixed / Crunchy
        this.globalRecipes.add(new Recipe("I was made for lovin' this",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                Collections.singletonList(catalog.getToppingList().get(0)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                1.5f));

        //Recipe 3: Plain / / White Chocolate, Milk Chocolate, Black Chocolate / Topped / Crunchy
        this.globalRecipes.add(new Recipe("Our generation",
                catalog.getDoughList().get(1),
                null,
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(1),
                        catalog.getToppingList().get(2)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(0),
                2.1f));

        //Recipe 4: Chocolate / / Topped / Chewy
        this.globalRecipes.add(new Recipe("Cookie for nothing",
                catalog.getDoughList().get(2),
                null,
                new ArrayList<>(),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                0.9f));

        //Recipe 5: Peanut Butter / Vanille / 3 White Chocolate / Mixed / Crunchy
        this.globalRecipes.add(new Recipe("White Dog",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(0),
                        catalog.getToppingList().get(0)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                2.0f));

        //Recipe 6: Plain / Vanilla / / Mixed / Chewy
        this.globalRecipes.add(new Recipe("Simple Cookie",
                catalog.getDoughList().get(1),
                catalog.getFlavorList().get(0),
                new ArrayList<>(),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(1),
                1.0f));

        //Recipe 7: Peanut Butter / Cinnamon / 2M&M's, White Chocolate / Mixed / Chewy
        this.globalRecipes.add(new Recipe("Dreams On",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(1),
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(3),
                        catalog.getToppingList().get(3)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(1),
                2.0f));

        //Recipe 8: Chocolate / Chili / Black Chocolate / Topped / Crunchy
        this.globalRecipes.add(new Recipe("Cooked to be Wild",
                catalog.getDoughList().get(2),
                catalog.getFlavorList().get(2),
                Collections.singletonList(catalog.getToppingList().get(2)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(0),
                1.8f));

        //Recipe 9: Plain / Vanilla / White Chocolate, Black Chocolate / Topped / Chewy
        this.globalRecipes.add(new Recipe("You should stay",
                catalog.getDoughList().get(2),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(2)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                2.0f));

        //Recipe 10: Oatmeal / Oyster / Reese's buttercup / Topped / Chewy
        this.globalRecipes.add(new Recipe("Don't fear this cookie",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(3),
                Collections.singletonList(catalog.getToppingList().get(4)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                2.5f));
    }


    /**
     * @param recipe current recipe to check
     */
    private boolean checkRecipeExists(Recipe recipe) {
        return globalRecipes.contains(recipe);
    }

    Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password);
        if (saveCustomerAccountIfAbsent(newAccount, email)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }

    public Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password, Order temporaryOrder) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password, temporaryOrder);
        if (saveCustomerAccountIfAbsent(newAccount, email)) {
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

    private void addCustomerToLoyaltyProgram(Customer customer) {
        customer.addToLoyaltyProgram();
    }

}