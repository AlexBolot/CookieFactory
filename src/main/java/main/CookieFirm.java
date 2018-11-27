package main;

import api.BankAPI;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import store.Manager;
import store.Store;

import java.util.*;
import java.util.stream.Collectors;

public class CookieFirm {

    private Set<Guest> guests = new HashSet<>();
    private Set<Customer> accounts = new HashSet<>();
    private List<Store> stores = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();
    private List<Recipe> globalRecipes = new ArrayList<>();
    private BankAPI bankAPI = new BankAPI();

    private static boolean inflated = false;
    private static CookieFirm cookieFirm = new CookieFirm();

    public static CookieFirm instance() {
        return cookieFirm;
    }

    private CookieFirm() {

        Catalog catalog = new Catalog();

        //Recipe 1: Peanut Butter / Chili / / Mixed / Crunchy
        this.globalRecipes.add(new Recipe("Pathway to hell",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(2),
                new ArrayList<>(),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                false));

        //Recipe 2: Oatmeal / Vanilla / White Chocolate / Mixed / Crunchy
        this.globalRecipes.add(new Recipe("I was made for lovin' this",
                catalog.getDoughList().get(0),
                catalog.getFlavorList().get(0),
                Collections.singletonList(catalog.getToppingList().get(0)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                false));

        //Recipe 3: Plain / / White Chocolate, Milk Chocolate, Black Chocolate / Topped / Crunchy
        this.globalRecipes.add(new Recipe("Our generation",
                catalog.getDoughList().get(1),
                null,
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(1),
                        catalog.getToppingList().get(2)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(0),
                false));

        //Recipe 4: Chocolate / / Topped / Chewy
        this.globalRecipes.add(new Recipe("Cookie for nothing",
                catalog.getDoughList().get(2),
                null,
                new ArrayList<>(),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                false));

        //Recipe 5: Peanut Butter / Vanille / 3 White Chocolate / Mixed / Crunchy
        this.globalRecipes.add(new Recipe("White Dog",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(0),
                        catalog.getToppingList().get(0)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(0),
                false));

        //Recipe 6: Plain / Vanilla / / Mixed / Chewy
        this.globalRecipes.add(new Recipe("Simple Cookie",
                catalog.getDoughList().get(1),
                catalog.getFlavorList().get(0),
                new ArrayList<>(),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(1),
                false));

        //Recipe 7: Peanut Butter / Cinnamon / 2M&M's, White Chocolate / Mixed / Chewy
        this.globalRecipes.add(new Recipe("Dreams On",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(1),
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(3),
                        catalog.getToppingList().get(3)),
                catalog.getMixList().get(0),
                catalog.getCookingList().get(1),
                false));

        //Recipe 8: Chocolate / Chili / Black Chocolate / Topped / Crunchy
        this.globalRecipes.add(new Recipe("Cooked to be Wild",
                catalog.getDoughList().get(2),
                catalog.getFlavorList().get(2),
                Collections.singletonList(catalog.getToppingList().get(2)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(0),
                false));

        //Recipe 9: Plain / Vanilla / White Chocolate, Black Chocolate / Topped / Chewy
        this.globalRecipes.add(new Recipe("You should stay",
                catalog.getDoughList().get(2),
                catalog.getFlavorList().get(0),
                Arrays.asList(catalog.getToppingList().get(0),
                        catalog.getToppingList().get(2)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                false));

        //Recipe 10: Oatmeal / Oyster / Reese's buttercup / Topped / Chewy
        this.globalRecipes.add(new Recipe("Don't fear this cookie",
                catalog.getDoughList().get(3),
                catalog.getFlavorList().get(3),
                Collections.singletonList(catalog.getToppingList().get(4)),
                catalog.getMixList().get(1),
                catalog.getCookingList().get(1),
                false));
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

    public Optional<Customer> findCustomer(String email) {
        Customer customer = null;
        for (Customer account : this.accounts) {
            if (account.getEmail().equals(email)) {
                customer = account;
            }
        }
        return Optional.ofNullable(customer);
    }

    Optional<Store> findStore(String name) {
        Store store = null;
        for (Store s : this.stores) {
            if (s.getName().equals(name))
                store = s;
        }
        return Optional.ofNullable(store);
    }

    public void inflate(List<Store> stores, List<Manager> managers){
        this.managers = managers;
        this.stores = stores;

        CookieFirm.inflated = true;
    }

    void addGuest(Guest guest) {
        guests.add(guest);
    }

    public List<Store> getStores() {
        return stores;
    }

    public void addStore(Store s) {
        stores.add(s);
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public void setBankAPI(BankAPI bankAPI) {
        this.bankAPI = bankAPI;
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

    public BankAPI getBankAPI() {
        return bankAPI;
    }

    public void addCustomerToLoyaltyProgram(Customer customer) {
        customer.addToLoyaltyProgram();
    }

}