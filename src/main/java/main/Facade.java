package main;

import api.BankingData;
import order.Order;
import recipe.Recipe;
import recipe.ingredient.*;
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

    //TODO remove since unused
    public void addOpeningClosingTimeFromNow(String manager, String dayName, int behindHours, int aheadHours) {
        DayOfWeek day = dayFromName(dayName);
        LocalTime opTime = LocalTime.now().minusHours(behindHours).plusSeconds(0).plusNanos(0);
        LocalTime clTime = LocalTime.now().plusHours(aheadHours).plusSeconds(0).plusNanos(0);

        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        manager1.ifPresent(manager2 -> managerChangeTime(manager2, day, opTime, clTime));
    }

    public void managerChangeOpeningClosingTime(String manager, String dayName, String beginHours,
                                                String endHours) {
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if (manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, transformeStringToTime(beginHours));
            manager1.get().changeClosingTime(day, transformeStringToTime(endHours));
        }
    }

    public void managerChangeOpeningTime(String manager, String dayName, String beginHours) {
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeOpeningTime(day, transformeStringToTime(beginHours)));
    }

    public void managerChangeClosingTime(String manager, String dayName, String endHours) {
        DayOfWeek day = dayFromName(dayName);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        manager1.ifPresent(manager2 -> manager2.changeClosingTime(day, transformeStringToTime(endHours)));
    }


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

    public void managerChangeIngredientMargin(String managerName, String type, String ingredientName, double newMargin) {
        Ingredient ingredient;
        switch (type) {
            case "dough":
                ingredient = cookieFirm.getCatalog().doughFromName(ingredientName);
                break;
            case "flavor":
                ingredient = cookieFirm.getCatalog().flavorFromName(ingredientName);
                break;
            case "topping":
                ingredient = cookieFirm.getCatalog().toppingFromName(ingredientName);
                break;
            default:
                return;
        }

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

    //UTILS MANAGER
    private LocalTime transformeStringToTime(String time) {
        int opHour = Integer.parseInt(time.split(":")[0]);
        int opMinutes = Integer.parseInt(time.split(":")[1]);
        return LocalTime.of(opHour, opMinutes);
    }

    private void managerChangeTime(Manager m, DayOfWeek day, LocalTime opTime, LocalTime clTime) {
        m.changeOpeningTime(day, opTime);
        m.changeClosingTime(day, clTime);
    }


    //PART GUEST

    public Integer createGuest() {
        Guest guest = new Guest();
        this.cookieFirm.addGuest(guest);
        return guest.getId();
    }

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

    public void guestAddStoreToOrder(int id, String sStore) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
        }
    }


    //GUEST ADD PICKUPTIME

    //TODO remove since unused
    public void guestAddPickTimeToOrder(int id, int time, String pickupDay) {
        //Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        //opGuest.ifPresent(guest -> guest.getTemporaryOrder().setPickUpTime(generateTime(time, pickupDay)));
    }

    /**
     * Allows a Guest to set the pickup time, day and store to an order
     *
     * @param id        ID used to reference the Guest
     * @param storeName Name of the store to order from
     * @param hours     Hours of the day (ex : 16 -> 16:--)
     * @param minutes   Minutes of the day (ex : 34 -> --:34)
     * @param pickupDay Name of the day (ex : "Monday")
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

    //TODO remove since unused
    public void guestAddPickUpTimeAndStoreToOrder(int id, String sStore, int time, String pickupDay) {
        /*Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(time, pickupDay));
        }*/
    }

    //GUEST ADD OR REMOVE COOKIES
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

    //TODO remove since unused
    public void guestPlaceOrderWithCookies(int id, String sStore, int nbCookies, int pickupTime, String pickUpDay, Boolean payedOnline) {
        /*Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.addCookie(opStore.get().getMonthlyRecipe(), nbCookies);
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(pickupTime, pickUpDay));

            opGuest.get().placeOrder(payedOnline);
        }*/
    }

    public void guestPlaceOrder(int id, Boolean payedOnline) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);

        opGuest.ifPresent(guest -> guest.placeOrder(payedOnline));
    }

    //TODO permettre la création de custom recipe
    /*
    public void guestOrderCustomRecipe(){
    }
    */
    public void guestValidateHisOrder(int id, String email, boolean pay) {
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        if (opGuest.isPresent()) {
            opGuest.get().setEmail(email);
            opGuest.get().placeOrder(pay);
        } else throw new IllegalArgumentException("Could not find Guest with id = " + id);
    }


    public void setBankingDataCustomer(String sEmail, String accountId) {
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        if (opCustomer.isPresent()) {
            BankingData bankingData = new BankingData(opCustomer.get().getFirstName(), opCustomer.get().getLastName(), accountId);
            opCustomer.get().setBankingData(bankingData);
        }

    }


    //CUSTOMER RELATED
    public Integer createACustomer(String sName, String sLastName, String phoneNumber, String email, String password) {
        Customer customer = this.cookieFirm.createAccount(sName, sLastName, phoneNumber, email, password);
        return customer.getId();
    }


    public boolean addACustomerToLP(String sEmail) {
        Optional<Customer> customer = this.cookieFirm.findCustomer(sEmail);
        if (customer.isPresent()) {
            this.cookieFirm.addCustomerToLoyaltyProgram(customer.get());
            return true;
        }
        return false;
    }


    //TODO ajouter dans le bank id ajouter juste avec le accountID


    //TODO remove since unused
    public boolean anEmployeeMakeAnActionOnOrder(String sStore, int time, String day, String email, String action) {
        /*Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opStore.isPresent()) {
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(time, day), email);

            if (opOrder.isPresent()) {
                if (action.toUpperCase().equals("CANCELED")) {
                    opOrder.get().cancel();
                    return true;
                } else if (action.toUpperCase().equals("WITHDRAWN")) {
                    opOrder.get().withdraw();
                    return true;
                }
            }
        }*/
        return false;
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
    public boolean anEmployeeMakeAnActionOnOrder(String storeName, int hours, int minutes, String day, String email, String action) {
        Optional<Store> opStore = this.cookieFirm.findStore(storeName);

        if (opStore.isPresent()) {
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(hours, minutes, day), email);

            if (opOrder.isPresent()) {
                switch (action.toUpperCase()) {
                    case "CANCELED":
                        opOrder.get().cancel();
                        return true;
                    case "WITHDRAWN":
                        opOrder.get().withdraw();
                        return true;
                    default:
                        throw new IllegalArgumentException("Unkown action on order : " + action);
                }
            }
        }
        return false;
    }

    //TODO remove since unused
    public String anEmployeeSearchAnOrderState(String sStore, int time, String day, String email) {
        /*Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opStore.isPresent()) {
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(time, day), email);

            if (opOrder.isPresent()) {
                return opOrder.get().getState().toString();
            }
        }*/
        return "none";
    }

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

    public void addStockForTopping(String store, String type, String ingredient, int quantity) {
        Optional<Store> opStore = this.cookieFirm.findStore(store);
        opStore.ifPresent(store1 -> store1.getKitchen().refill(ingredientFromName(type, ingredient), quantity));
    }

    //TODO remove since unused
    public LocalDateTime generateTime(int pickupTime, String pickUpDay) {
        /*Clock clock = CookieFirm.instance().getClock();
        LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault()).withSecond(0).withNano(0);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(pickUpDay.toUpperCase());
        return now.plusHours(pickupTime).with(TemporalAdjusters.nextOrSame(dayOfWeek));*/
        return null;
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
