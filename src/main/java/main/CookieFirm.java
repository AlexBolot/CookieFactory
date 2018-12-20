package main;

import api.BankAPI;
import com.sun.org.glassfish.external.statistics.Statistic;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.Catalog;
import statistics.IStoreStat;
import store.Manager;
import store.Store;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton representing The Cookie Factory
 * Handle the accounts, the stores, and the global recipee and ingredient catalog.
 */
public class CookieFirm {

    private Set<Guest> guests = new HashSet<>();
    private Set<Customer> accounts = new HashSet<>();
    private List<Store> stores = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();
    private List<Recipe> globalRecipes = new ArrayList<>();
    private BankAPI bankAPI = new BankAPI();
    private Catalog catalog;

    private Clock clock = Clock.systemUTC();

    private static boolean inflated = false;
    private static CookieFirm cookieFirm = new CookieFirm();

    public static CookieFirm instance() {
        return cookieFirm;
    }

    /**
     * Private constructor, should never be called outside the class, for singleton use
     */
    private CookieFirm() {

        this.catalog = new Catalog();

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

        //Recipe 5: Peanut Butter / Vanilla / 3 White Chocolate / Mixed / Crunchy
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

    /**
     * Create a customer account form the passed infomation
     *
     * @param firstName   {@link String} The firstname of the account to be creaated
     * @param lastName    {@link String} The lastname of the account to be creaated
     * @param phoneNumber {@link String} The phoneNumber of the account to be creaated
     * @param email       {@link String} The email of the account to be creaated
     * @param password    {@link String} The password of the account to be creaated
     * @return Customer created
     * @throws IllegalArgumentException if the email is alreay in use
     */
    Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password);
        if (saveCustomerAccountIfAbsent(newAccount)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }

    /**
     * Create a customer account with a temporary Order
     *
     * @param firstName      {@link String} The firstname of the account to be created
     * @param lastName       {@link String} The lastname of the account to be created
     * @param phoneNumber    {@link String} The phoneNumber of the account to be created
     * @param email          {@link String} The email of the account to be created, used as an unique identifier
     *                       If an account already exists with this email will throw
     * @param password       {@link String} The password of the account to be created
     * @param temporaryOrder {@link Order} The current order of the account to be created
     * @return Customer created
     * @throws IllegalArgumentException if the email is alreay in use
     */
    public Customer createAccount(String firstName, String lastName, String phoneNumber, String email, String password, Order temporaryOrder) {
        Customer newAccount = new Customer(firstName, lastName, phoneNumber, email, password, temporaryOrder);
        if (saveCustomerAccountIfAbsent(newAccount)) {
            return newAccount;
        } else {
            throw new IllegalArgumentException("An account already exists with this email");
        }
    }

    /**
     * Register a customer if the account does not exists in the account list
     *
     * @param customer {@link Customer} to be registered
     * @return false if already in the account list, otherwise the new account is saved and return true
     */
    boolean saveCustomerAccountIfAbsent(Customer customer) {
        for (Customer account : this.accounts) {
            if (account.getEmail().equals(customer.getEmail())) {
                return false;
            }
        }
        guests.removeIf((guest -> guest.getId() == customer.getId()));
        guests = guests.stream().filter(g -> !customer.getEmail().equals(g.getEmail())).collect(Collectors.toSet());
        accounts.add(customer);
        return true;
    }

    /**
     * Return a customer from an email
     *
     * @param email {@link String} The target customer email. The optional will be empty if no customer with the email is found
     * @return {@link Optional<Customer>} Optional of the customer with the passed email, empty if not found.
     */
    public Optional<Customer> findCustomer(String email) {
        Customer customer = null;
        for (Customer account : this.accounts) {
            if (account.getEmail().equals(email)) {
                customer = account;
            }
        }
        return Optional.ofNullable(customer);
    }

    /**
     * Return a customer from an given id
     *
     * @param id The target customer ID. The optional will be empty if no customer with this ID is found
     * @return {@link Optional<Customer>} Optional of the customer with the given id, empty if not found.
     */
    public Optional<Customer> findCustomer(int id) {
        return this.accounts.stream()
                .filter(account -> account.getId() == id)
                .findFirst();
    }

    /**
     * Return a guest from an id
     *
     * @param id {@link Integer} The target guest id. The optional will be empty if no guest with the id is found
     * @return {@link Optional<Guest>} Optional of the customer with the passed id, empty if not found.
     */
    public Optional<Guest> findGuest(int id) {
        Guest guest = null;
        for (Guest account : this.guests) {
            if (account.getId() == id) {
                guest = account;
            }
        }
        return Optional.ofNullable(guest);
    }

    /**
     * Return a manager from an email
     *
     * @param name {@link String} The target customer name. The optional will be empty if no manager with the name is found
     * @return {@link Optional<Manager>} Optional of the manager with the passed name, empty if not found.
     */
    public Optional<Manager> findManager(String name) {
        Manager manager1 = null;
        for (Manager manager : managers) {
            if (manager.getName().equals(name)) {
                manager1 = manager;
            }
        }
        return Optional.ofNullable(manager1);
    }

    /**
     * Return a guest with the passed id considering the customers also.
     *
     * @param id {@link Integer} The target guest id. The optional will be empty if no guest with the id is found
     * @return {@link Optional<Customer>} Optional of the customer with the passed email, empty if not found.
     */
    public Optional<Guest> findGuestOrCustomer(int id) {
        Set<Guest> guests = getAllGuests();
        Guest guest = null;
        for (Guest account : guests) {
            if (account.getId() == id) {
                guest = account;
            }
        }
        return Optional.ofNullable(guest);
    }

    /**
     * Return a store from an name
     *
     * @param name {@link String} The target store name. The optional will be empty if no store with the name is found
     * @return {@link Optional<Customer>} Optional of the store with the passed name, empty if not found.
     */
    public Optional<Store> findStore(String name) {
        Store store = null;
        for (Store s : this.stores) {
            if (s.getName().equals(name))
                store = s;
        }
        return Optional.ofNullable(store);
    }

    /**
     * Find a recipee based on it's name
     *
     * @param recipeeName {@link String} recipee name
     * @return {@link Optional<Recipe>} The search result, empty if no recipee were found.
     */
    public Optional<Recipe> findRecipee(String recipeeName) {
        return this.getGlobalRecipes().stream().filter(recipe -> recipe.getName().equalsIgnoreCase(recipeeName)).findFirst();
    }

    /**
     * @deprecated was used once, not anymore, the mistery persists, should be erased.
     */
    @Deprecated
    public void inflate(List<Store> stores, List<Manager> managers) {
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

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    Set<Guest> getGuests() {
        return guests;
    }

    Set<Guest> getAllGuests() {
        return Stream.concat(guests.stream(), accounts.stream()).collect(Collectors.toSet());
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

    public Catalog getCatalog() {
        return catalog;
    }

    public void addCustomerToLoyaltyProgram(Customer customer) {
        customer.addToLoyaltyProgram();
    }

    public String aggregateStats (IStoreStat statistic) {
        StringBuilder output = new StringBuilder("{");
        for (Store store : stores) {
            statistic.setStore(store);
            statistic.computeValue();
            output.append("\"").append(store.getName()).append("\":").append(statistic.serialize());
        }
        return output+"}";
    }


}