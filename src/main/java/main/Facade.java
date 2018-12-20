package main;

import api.BankingData;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.Ingredient;
import recipe.ingredient.Topping;
import statistics.CookieRatioStat;
import store.Manager;
import store.Store;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Facade {

    private CookieFirm cookieFirm;

    public Facade() {
        this.cookieFirm = CookieFirm.instance();
    }

    /**
     * Add new store to cookie firm
     * @param sStore name of the new store
     * @param tax of the new store
     * @param margin on the custom recipe of the new store
     * @return the new Store
     */
    public Store addAStoreToFirm(String sStore, int tax, int margin) {
        Store store = new Store(sStore, tax, margin);
        cookieFirm.addStore(store);
        return store;
    }

    /**
     * Add a topping to the cookie firm ingredient catalog
     * If a topping with the same name already exists it will return this topping .
     *
     * @param toppingName {@link String} toppings name
     */
    public void addTopping(String toppingName) {
        cookieFirm.getCatalog().addTopping(toppingName);
    }

    /**
     * Add a dough to the cookie firm ingredient catalog
     * If a dough with the same name already exists it will return this dough .
     *
     * @param doughName {@link String} dough name
     */
    public void addDough(String doughName) {
        cookieFirm.getCatalog().addDough(doughName);
    }

    /**
     * Add a flavor to the cookie firm ingredient catalog
     * If a flavor with the same name already exists it will return this flavor .
     *
     * @param flavorName {@link String} flavor name
     */
    public void addFlavor(String flavorName) {
        cookieFirm.getCatalog().addFlavor(flavorName);
    }

    public void addManagerToStore(String manager, String store) {
        Optional<Store> store1 = cookieFirm.findStore(store);
        if (store1.isPresent()) {
            Manager manager1 = new Manager(store1.get(), manager);
            cookieFirm.addManager(manager1);
        }
    }


    //MANAGER

    /**
     * Manager change the opening and closing time of one day for his store
     * @param manager name of the manager
     * @param dayName day to modify schedule
     * @param beginHours hour of opening
     * @param beginMinute minute of opening
     * @param endHours hour of closing
     * @param endMinute minute of closing
     */
    public void managerChangeOpeningClosingTime(String manager, String dayName,
                                                int beginHours,
                                                int beginMinute,
                                                int endHours,
                                                int endMinute) {
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if (manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, LocalTime.of(beginHours, beginMinute, 0,0));
            manager1.get().changeClosingTime(day, LocalTime.of(endHours, endMinute, 0,0));
        }
    }

    /**
     * Manager change the opening time for one day
     * @param manager name of the manager
     * @param dayName day to modify schedule
     * @param beginHours hour of opening
     * @param beginMinute minute of opening
     */
    public void managerChangeOpeningTime(String manager, String dayName, int beginHours, int beginMinute) {
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeOpeningTime(day, LocalTime.of(beginHours, beginMinute, 0,0)));
    }

    /**
     * Manager change the closing time for one day
     * @param manager name of the manager
     * @param dayName day to modify schedule
     * @param endHours hour of closing
     * @param endMinute minute of closing
     */
    public void managerChangeClosingTime(String manager, String dayName, int endHours, int endMinute) {
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeClosingTime(day,LocalTime.of(endHours, endMinute, 0,0)));
    }


    /**
     * Manager add a new Monthly cookie
     * @param manager name of the manager
     * @param recipeName name of the new recipe
     * @param dough of the new recipe
     * @param flavor of the new recipe(optional)
     * @param topping of the new recipe
     * @param topping2 of the new recipe (optional)
     * @param topping3 of the new recipe(optional)
     * @param mix of the new recipe
     * @param cooking of the new recipe
     */
    public void managerAddMonthlyCookie(String manager, String recipeName, String dough, String flavor,
                                        String topping, String topping2, String topping3,
                                        String mix,
                                        String cooking) {
        Recipe recipe = new Recipe(recipeName,
                cookieFirm.getCatalog().doughFromName(dough),
                cookieFirm.getCatalog().flavorFromName(flavor),
                createToppingList(topping, topping2, topping3),
                cookieFirm.getCatalog().mixFromName(mix),
                cookieFirm.getCatalog().cookingFromName(cooking),
                false);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeMontlyRecipe(recipe));
    }

    /**
     * Manager change an ingredient margin
     * @param managerName name of the manager
     * @param type of the ingredient
     * @param ingredientName name of the ingredient
     * @param newMargin new margin for the ingredient
     */
    public void managerChangeIngredientMargin(String managerName, String type, String ingredientName, double newMargin) {
        Ingredient ingredient = ingredientFromName(type, ingredientName);
        Optional<Manager> opManager = cookieFirm.findManager(managerName);
        opManager.ifPresent(manager -> manager.changeIngredientMargin(ingredient, newMargin));
    }

    /**
     * Allows a manager to change the "reward-value to cash" ratio of his store
     *
     * @param managerName Name of the manager
     * @param ratio       New ratio of the manager's store's UnFaitProgramm
     */
    public void managerChangeUnFaithPassRewardRatio(String managerName, double ratio) {
        Optional<Manager> opManager = cookieFirm.findManager(managerName);
        opManager.ifPresent(manager -> manager.changeRewardPointsToValueRatio(ratio));
    }

    /**
     * Allow a manager to change the supplier price of an ingredient
     *
     * @param managerName {@link String} manager's name
     * @param name        {@link String} ingredient name
     * @param price       double desired price
     * @throws IllegalArgumentException if ingredient is unknowned
     */
    public void managerChangeIngredientSuplierPrice(String managerName, String type, String name, double price) {
        Optional<Manager> opManager = cookieFirm.findManager(managerName);
        Ingredient ingredient = ingredientFromName(type, name);
        if (ingredient == null)
            throw new IllegalArgumentException("Unknown Ingredient Name");
        opManager.ifPresent(manager -> manager.changeIngredientSupplierPrice(ingredient, price));

    }

    /**
     * Allows a manager to query the unit price of a recipee
     *
     * @param managerName {@link String} name of the manager
     * @param recipeeName {@link String} name of the rercipee
     * @return float Unit price of the recipee at the manager's store
     */
    public double managerQueryPriceOf(String managerName, String recipeeName) {
        Optional<Manager> opManager = cookieFirm.findManager(managerName);
        Manager manager = opManager.orElseThrow(() -> new IllegalArgumentException("Unknown manager name"));

        Optional<Recipe> opRecipee = this.cookieFirm.findRecipee(recipeeName);

        Store store = manager.getStore();
        Recipe recipee;
        if (!opRecipee.isPresent() && !store.getMonthlyRecipe().getName().equalsIgnoreCase(recipeeName))
            throw new IllegalArgumentException("Unknown Recipee Name");
        else
            recipee = opRecipee.orElse(store.getMonthlyRecipe());

        return store.getRecipePrice(recipee);
    }

    /**
     * The json representation of the cookie recipe ratio for a manager store.
     * The custom recipees are regrouped under one statistics
     *
     * @param managerName {@link String} manager's name
     * @return {@link String} JSON representation {"recipee name":"Ratio"....}
     */
    public String managerQueriesCookieRatio(String managerName) {
        Optional<Manager> opManager = cookieFirm.findManager(managerName);
        if (!opManager.isPresent())
            throw new IllegalArgumentException("Unknown manager");
        Manager manager = opManager.get();
        CookieRatioStat stat = new CookieRatioStat(manager.getStore());
        stat.computeValue();
        return stat.serialize();
    }

    //UTILS MANAGER

    /**
     * Given a sting parse it to transform it in localtime
     * @param time string of time
     * @return a local time corresponding to the string
     */
    private LocalTime transformeStringToTime(String time) {
        int opHour = Integer.parseInt(time.split(":")[0]);
        int opMinutes = Integer.parseInt(time.split(":")[1]);
        return LocalTime.of(opHour, opMinutes);
    }

    /**
     * Manager change the closing and opening time of one day for his store
     * @param m object manager
     * @param day object representing the day
     * @param opTime local time representing the opening time
     * @param clTime local time representing the closing time
     */
    private void managerChangeTime(Manager m, DayOfWeek day, LocalTime opTime, LocalTime clTime) {
        m.changeOpeningTime(day, opTime);
        m.changeClosingTime(day, clTime);
    }


    //PART GUEST

    /**
     * Create a new guest
     * @return new guest id
     */
    public Integer createGuest() {
        Guest guest = new Guest();
        this.cookieFirm.addGuest(guest);
        return guest.getId();
    }

    /**
     * Guest create an account when validating his order
     * @param idGuest current id guest
     * @param fName name of the guest
     * @param lastN last name of the guest
     * @param phone of the guest
     * @param email of the guest
     * @param password of the guest
     * @return id of the customer created
     */
    public Optional<Integer> guestCreateAccount(int idGuest, String fName, String lastN, String phone, String email,
                                                String password) {
        Optional<Guest> opGuest = this.cookieFirm.findGuest(idGuest);
        Integer id = null;
        if (opGuest.isPresent()) {
            Customer customer = Customer.from(opGuest.get(), fName, lastN, phone, email, password);
            this.cookieFirm.saveCustomerAccountIfAbsent(customer);
            id = customer.getId();
        }
        return Optional.ofNullable(id);
    }

    /**
     * Guest add a store to his order
     * @param id of the current guest
     * @param sStore name of the store to link to the order
     */
    public void guestAddStoreToOrder(int id, String sStore) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
        }
    }


    //GUEST ADD PICKUPTIME

    /**
     * Guest add a pick time,day and store to his order
     * @param id of the current guest
     * @param storeName name of the store where to place this order
     * @param hours when to pick up the order
     * @param minutes when to pick up the order
     * @param pickupDay when to pick up the order
     */
    public void guestAddPickUpTimeAndStoreToOrder(int id, String storeName, int hours, int minutes, String pickupDay) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(storeName);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(hours, minutes, pickupDay));
        }
    }

    //GUEST ADD OR REMOVE COOKIES

    /**
     * Guest remove or add a quantity of a cookie
     * @param id of the current guest
     * @param sStore link to the order
     * @param quantity to add or remove
     * @param recipeName name of the recipe to modify
     * @param remove boolean to remove or add the quantity
     */
    public void guestAddOrRemoveCookie(int id, String sStore, int quantity, String recipeName, boolean remove) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            opGuest.get().getTemporaryOrder().setStore(opStore.get());
            for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
                if (cookie.getName().equals(recipeName)) {
                    if (!remove) {
                        opGuest.get().getTemporaryOrder().addCookie(cookie, quantity);
                    } else
                        opGuest.get().getTemporaryOrder().removeCookie(cookie, quantity);
                    return;
                }
            }
        }
    }

    //GUEST ORDER CUSTOM COOKIE

    /**
     * Guest order a custom cookie
     * @param id of the current guest
     * @param dough of the custom cookie
     * @param flavor of the custom cookie (optional)
     * @param topping of the custom cookie
     * @param topping2 of the custom cookie (optional)
     * @param topping3 of the custom cookie (optional)
     * @param mix of the custom cookie
     * @param cooking of the custom cookie
     * @param quantity to order of the custom cookie
     */
    public void guestOrderCustomCookie(int id, String dough, String flavor,
                                       String topping, String topping2, String topping3,
                                       String mix,
                                       String cooking, int quantity) {

        Optional<Guest> guest = cookieFirm.findGuestOrCustomer(id);
        guest.ifPresent(guest1 -> guest1.orderCustomRecipe(quantity,
                cookieFirm.getCatalog().doughFromName(dough),
                cookieFirm.getCatalog().flavorFromName(flavor),
                createToppingList(topping, topping2, topping3),
                cookieFirm.getCatalog().mixFromName(mix),
                cookieFirm.getCatalog().cookingFromName(cooking)));
    }

    // GUEST PLACE ORDER
    /**
     * Customer place his order
     * @param id of the current Customer
     * @param payedOnline boolean that indicate if the order is payed online or not
     */
    public void guestPlaceOrder(int id , Boolean payedOnline){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        opGuest.ifPresent(guest -> guest.placeOrder(payedOnline));
    }


    /**
     * Guest validate his order
     * @param id of the current guest
     * @param email used to validate the order
     * @param pay boolean that indicate if the order is payed online or not
     */
    public void guestValidateHisOrder(int id, String email, boolean pay) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        if (opGuest.isPresent()) {
            opGuest.get().setEmail(email);
            opGuest.get().placeOrder(pay);
        } else throw new IllegalArgumentException("Could not find Guest with id = " + id);
    }


    /**
     * Guest add banking data to pay his order online
     * @param id of the current customer
     * @param name related to the banking account
     * @param lastName related to the banking account
     * @param accountID id of the banking account
     */
    public void guestAddBankingData(int id, String name, String lastName, String accountID){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        opGuest.ifPresent(guest -> guest.setBankingData(new BankingData(name, lastName, accountID)));
    }


    //CUSTOMER RELATED

    /**
     * Customer add his banking data
     * @param sEmail of the customer
     * @param accountId of his banking data
     */
    public void setBankingDataCustomer(String sEmail, String accountId) {
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        if (opCustomer.isPresent()) {
            BankingData bankingData = new BankingData(opCustomer.get().getFirstName(), opCustomer.get().getLastName(), accountId);
            opCustomer.get().setBankingData(bankingData);
        }

    }


    /**
     * Create an account
     * @param sName of the customer
     * @param sLastName of the customer
     * @param phoneNumber of the customer
     * @param email of the customer
     * @param password of the customer
     * @return the id of the new customer
     */
    public Integer createACustomer(String sName, String sLastName, String phoneNumber, String email, String password) {
        Customer customer = this.cookieFirm.createAccount(sName, sLastName, phoneNumber, email, password);
        return customer.getId();
    }

    /**
     * Add customer to fidelity program
     * @param sEmail of the customer
     * @return if the customer was add to program or not
     */
    public boolean addACustomerToLP(String sEmail) {
        Optional<Customer> customer = this.cookieFirm.findCustomer(sEmail);
        if (customer.isPresent()) {
            this.cookieFirm.addCustomerToLoyaltyProgram(customer.get());
            return true;
        }
        return false;
    }




    //EMPLOYEE RELATED
    /**
     * Allows an Employee to know the current state of an order
     *
     * @param storeName Name of the store containing the order
     * @param hours     Hours of the order's pickupTime (used to find the order)
     * @param minutes   Minutes of the order's pickupTime (used to find the order)
     * @param day       Day of the order's pickupTime (used to find the order)
     * @param email     Email of the Guest (owner of the order, used to find the order)
     * @return String value representing the state of the order
     */
    public String anEmployeeSearchAnOrderState(String storeName, int hours, int minutes, String day, String email) {
        Optional<Store> opStore = this.cookieFirm.findStore(storeName);

        if (opStore.isPresent()) {
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(hours, minutes, day), email);

            if (opOrder.isPresent()) {
                return opOrder.get().getState().toString();
            }
        }
        return "none";
    }

    /**
     * Allows an Employee to interact with an order from a store
     *
     * @param storeName Name of the store containing the order
     * @param hours     Hours of the order's pickupTime (used to find the order)
     * @param minutes   Minutes of the order's pickupTime (used to find the order)
     * @param day       Day of the order's pickupTime (used to find the order)
     * @param email     Email of the Guest (owner of the order, used to find the order)
     * @param action    String value of the Action to do. Values allowed : [CANCELED, WITHDRAWN]
     * @return True if the action was successfully applyed of the order. False otherwise
     */
    public boolean anEmployeeMakeAnActionOnOrder(String storeName, int hours, int minutes, String day, String email, String action, int points, int freeCookie) {
        Optional<Store> opStore = this.cookieFirm.findStore(storeName);
        if (opStore.isPresent()) {
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(hours, minutes, day), email);
            if (opOrder.isPresent()) {
                switch (action.toUpperCase()) {
                    case "CANCELED":
                        opOrder.get().cancel();
                        return true;
                    case "WITHDRAWN":
                        opOrder.get().withdraw(points,freeCookie);
                        return true;
                    default:
                        throw new IllegalArgumentException("Unkown action on order : " + action);
                }
            }
        }
        return false;
    }

    /**
     * Allows an Employee to interact with an order from a store (WITHDRAW)
     * @param storeName Name of the store containing the order
     * @param hours     Hours of the order's pickupTime (used to find the order)
     * @param minutes   Minutes of the order's pickupTime (used to find the order)
     * @param day       Day of the order's pickupTime (used to find the order)
     * @param email     Email of the Guest (owner of the order, used to find the order)
     * @param action    String value of the Action to do. Values allowed : [CANCELED, WITHDRAWN]
     * @return True if the action was successfully applyed of the order. False otherwise
     */
    public boolean anEmployeeMakeAnActionOnOrder(String storeName, int hours, int minutes, String day, String email, String action) {
        return anEmployeeMakeAnActionOnOrder(storeName,hours,minutes,day,email,action,0,0);
    }

    /**
     * Employee add stock for a topping
     * @param store name of the store where the employee work
     * @param type of the ingredient
     * @param ingredient name of the ingredient
     * @param quantity add to stock
     */
    public void anEmployeeAddsStockForTopping(String store, String type, String ingredient, int quantity) {
        Optional<Store> opStore = this.cookieFirm.findStore(store);
        opStore.ifPresent(store1 -> store1.getKitchen().refill(ingredientFromName(type, ingredient), quantity));
    }

    /**
     * Generates a LocalDateTime set at time of [hours:minutes] and date of [pickUpDay]
     * Note : The other values (month, year, etc) are based of the CookieFirm's clock.
     *
     * @param hours     Hours of the day
     * @param minutes   Minutes of the day
     * @param pickUpDay Day of the week
     * @return LocalDateTime set at time of [hours:minutes] and date of [pickUpDay]
     */
    public LocalDateTime generateTime(int hours, int minutes, String pickUpDay) {
        Clock clock = CookieFirm.instance().getClock();
        LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault()).withSecond(0).withNano(0);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(pickUpDay.toUpperCase());
        return now.withHour(hours).withMinute(minutes).with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }


    /**
     *
     * @param dayName name of the day
     * @return an object Day of Week
     */
    private DayOfWeek dayFromName(String dayName) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }

    /**
     *
     * @param type of the ingredient
     * @param ingredientName name of the ingredient
     * @return the corresponding ingredient
     */
    public Ingredient ingredientFromName(String type, String ingredientName) {
        switch (type) {
            case "dough":
                return cookieFirm.getCatalog().doughFromName(ingredientName);
            case "flavor":
                return cookieFirm.getCatalog().flavorFromName(ingredientName);
            case "topping":
                return cookieFirm.getCatalog().toppingFromName(ingredientName);
        }
        return null;
    }

    /**
     * Create a topping list given name of toppings
     * @param topping name of a topping
     * @param topping2 name of the second topping (optional)
     * @param topping3 name of the third topping (optional)
     * @return list of topping
     */
    private List<Topping> createToppingList(String topping, String topping2, String topping3) {
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add(cookieFirm.getCatalog().toppingFromName(topping));
        if (!topping2.equals("no topping"))
            toppingList.add(cookieFirm.getCatalog().toppingFromName(topping2));
        if (!topping3.equals("no topping"))
            toppingList.add(cookieFirm.getCatalog().toppingFromName(topping3));
        return toppingList;
    }



}
