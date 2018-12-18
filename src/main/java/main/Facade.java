package main;

import api.BankingData;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.*;
import store.Manager;
import store.Store;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Facade {

    private CookieFirm cookieFirm;

    public Facade(){
        this.cookieFirm = CookieFirm.instance();
    }

    /**
     * Add new store to cookie firm
     * @param sStore name of the new store
     * @param tax of the new store
     * @param margin on the custom recipe of the new store
     * @return the new Store
     */
    public Store addAStoreToFirm(String sStore, int tax, int margin){
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

    public void addManagerToStore(String manager, String store){
        Optional<Store> store1 = cookieFirm.findStore(store);
        if(store1.isPresent()) {
            Manager manager1 = new Manager(store1.get(), manager);
            cookieFirm.addManager(manager1);
        }
    }


    //MANAGER

    public void addOpeningClosingTimeFromNow(String manager,String dayName, int behindHours, int aheadHours){
        DayOfWeek day = dayFromName(dayName);
        LocalTime opTime = LocalTime.now().minusHours(behindHours).plusSeconds(0).plusNanos(0);
        LocalTime clTime = LocalTime.now().plusHours(aheadHours).plusSeconds(0).plusNanos(0);

        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        manager1.ifPresent(manager2 -> managerChangeTime(manager2, day, opTime, clTime));
    }

    public void managerChangeOpeningClosingTime(String manager, String dayName, String beginHours,
                                      String endHours){
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if(manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, transformeStringToTime(beginHours));
            manager1.get().changeClosingTime(day, transformeStringToTime(endHours));
        }
    }

    public void managerChangeOpeningTime(String manager, String dayName, String beginHours){
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeOpeningTime(day, transformeStringToTime(beginHours)));
    }

    public void managerChangeClosingTime(String manager, String dayName, String endHours){
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeClosingTime(day, transformeStringToTime(endHours)));
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
                                        String cooking){
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
     * @param ratio New ratio of the manager's store's UnFaitProgramm
     */
    public void managerChangeUnFaithPassRewardRatio(String managerName, double ratio){
        Optional<Manager> opManager = cookieFirm.findManager(managerName);
        opManager.ifPresent(manager -> manager.changeRewardPointsToValueRatio(ratio));
    }

    //UTILS MANAGER
    private LocalTime transformeStringToTime(String time){
        int opHour = Integer.parseInt(time.split(":")[0]);
        int opMinutes = Integer.parseInt(time.split(":")[1]);
        return LocalTime.of(opHour, opMinutes);
    }

    private void managerChangeTime(Manager m, DayOfWeek day, LocalTime opTime, LocalTime clTime){
        m.changeOpeningTime(day, opTime);
        m.changeClosingTime(day, clTime);
    }


    //PART GUEST

    /**
     * Create a new guest
     * @return new guest id
     */
    public Integer createGuest(){
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
                                  String password){
        Optional<Guest> opGuest = this.cookieFirm.findGuest(idGuest);
        Integer id = null;
        if(opGuest.isPresent()){
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
    public void guestAddStoreToOrder(int id, String sStore){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opGuest.isPresent() && opStore.isPresent()){
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
        }
    }


    //GUEST ADD PICKUPTIME

    /**
     * Guest add a pickup time to his order
     * @param id of the current guest
     * @param time pickup time of the order
     * @param pickupDay of the order
     */
    public void guestAddPickTimeToOrder(int id, int time, String pickupDay){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);

        opGuest.ifPresent(guest -> guest.getTemporaryOrder().setPickUpTime(generateTime(time, pickupDay)));
    }

    /**
     * Guest add pickup time, day and store to his order
     * @param id of the current guest
     * @param sStore name of the store to link to order
     * @param time pickup time of the order
     * @param pickupDay of the order
     */
    public void guestAddPickUpTimeAndStoreToOrder(int id, String sStore, int time, String pickupDay){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opGuest.isPresent() && opStore.isPresent()){
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(time, pickupDay));
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
                    }
                    else
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
                                       String cooking, int quantity){

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
     * Guest add cookie to his order and place his order at the same time
     * @param id of the current guest
     * @param sStore name of the store to link to the order
     * @param nbCookies to order
     * @param pickupTime pickup time of the order
     * @param pickUpDay pickup day of the order
     * @param payedOnline boolean that indicate if the order is payed online or not
     */
    public void guestPlaceOrderWithCookies(int id, String sStore, int nbCookies, int
            pickupTime, String pickUpDay, Boolean payedOnline) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.addCookie(opStore.get().getMonthlyRecipe(), nbCookies);
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(pickupTime, pickUpDay));

            opGuest.get().placeOrder(payedOnline);
        }
    }

    /**
     * Guest place his order
     * @param id of the current guest
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
    public void guestValidateHisOrder(int id, String email, boolean pay){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        if(opGuest.isPresent()) {
            opGuest.get().setEmail(email);
            opGuest.get().placeOrder(pay);
        }
        else throw new IllegalArgumentException("Could not find Guest with id = " + id);
    }



    //CUSTOMER RELATED

    /**
     * Customer add his banking data
     * @param sEmail of the customer
     * @param accountId of his banking data
     */
    public void setBankingDataCustomer(String sEmail, String accountId){
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        if(opCustomer.isPresent()) {
            BankingData bankingData = new BankingData(opCustomer.get().getFirstName(), opCustomer.get().getLastName(), accountId);
            opCustomer.get().setBankingData(bankingData);
        }

    }
    //TODO ajouter dans le bank id ajouter juste avec le accountID


    /**
     * Create an account
     * @param sName of the customer
     * @param sLastName of the customer
     * @param phoneNumber of the customer
     * @param email of the customer
     * @param password of the customer
     * @return the id of the new customer
     */
    public Integer createACustomer(String sName, String sLastName, String phoneNumber, String email, String
            password){
        Customer customer = this.cookieFirm.createAccount(sName, sLastName, phoneNumber, email, password);
        return customer.getId();
    }

    /**
     * Add customer to fidelity program
     * @param sEmail of the customer
     * @return if the customer was add to program or not
     */
    public boolean addACustomerToLP(String sEmail){
        Optional<Customer> customer = this.cookieFirm.findCustomer(sEmail);
        if(customer.isPresent()) {
            this.cookieFirm.addCustomerToLoyaltyProgram(customer.get());
            return true;
        }
        return false;
    }



    //EMPLOYEE RELATED

    /**
     * An employee make an action on an order (CANCELED, WITHDRAWN)
     * Search the order with datas
     * Not case sensitive
     * @param sStore name of the store where the employee work
     * @param time pickup time of the searched order
     * @param day pickup day of the searched order
     * @param email of the searched order's customer
     * @param action to make on the order
     * @return if the action could be done
     */
    public boolean anEmployeeMakeAnActionOnOrder(String sStore, int time, String day, String email, String action){
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opStore.isPresent()){
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(time, day), email);

            if(opOrder.isPresent()) {
                if(action.toUpperCase().equals("CANCELED")) {
                    opOrder.get().cancel();
                    return true;
                }
                else if(action.toUpperCase().equals("WITHDRAWN")){
                    opOrder.get().withdraw();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * An employee search and order to see its state
     * @param sStore name of the store where the employee work
     * @param time pickup time of the searched order
     * @param day pickup day of the searched order
     * @param email of the searched order's customer
     * @return the state of the order
     */
    public String anEmployeeSearchAnOrderState(String sStore, int time, String day, String email){
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opStore.isPresent()){
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(time, day), email);

            if(opOrder.isPresent()) {
                return opOrder.get().getState().toString();
            }
        }
        return "none";
    }

    /**
     * Employee add stock for a topping
     * @param store name of the store where the employee work
     * @param type of the ingredient
     * @param ingredient name of the ingredient
     * @param quantity add to stock
     */
    public void addStockForTopping(String store, String type, String ingredient, int quantity){
        Optional<Store> opStore = this.cookieFirm.findStore(store);
        opStore.ifPresent(store1 -> store1.getKitchen().refill(ingredientFromName(type, ingredient), quantity));
    }

    public LocalDateTime generateTime(int pickupTime, String pickUpDay) {
        return LocalDateTime.now().plusHours(pickupTime).with(TemporalAdjusters
                .next(DayOfWeek.valueOf(pickUpDay.toUpperCase()))).withSecond(0).withNano(0);
    }

    private DayOfWeek dayFromName(String dayName) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }


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


    private List<Topping> createToppingList(String topping, String topping2, String topping3){
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add(cookieFirm.getCatalog().toppingFromName(topping));
        if(!topping2.equals("no topping"))
            toppingList.add(cookieFirm.getCatalog().toppingFromName(topping2));
        if(!topping3.equals("no topping"))
            toppingList.add(cookieFirm.getCatalog().toppingFromName(topping3));
        return toppingList;
    }

}
