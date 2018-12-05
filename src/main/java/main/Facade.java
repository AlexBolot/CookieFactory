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


    public Store addAStoreToFirm(String sStore, int tax, int margin){
        Store store = new Store(sStore, tax, margin);
        cookieFirm.addStore(store);
        return store;
    }


    public void addManagerToStore(String manager, String store){
        Optional<Store> store1 = cookieFirm.findStore(store);
        if(store1.isPresent()) {
            Manager manager1 = new Manager(store1.get(), manager);
            cookieFirm.addManager(manager1);
        }
    }
    public void addOpeningClosingTimeFromNow(String manager,String dayName, int behindHours, int aheadHours){
        DayOfWeek day = dayFromName(dayName);
        LocalTime opTime = LocalTime.now().minusHours(behindHours).plusSeconds(0).plusNanos(0);
        LocalTime clTime = LocalTime.now().plusHours(aheadHours).plusSeconds(0).plusNanos(0);

        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if(manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, opTime);
            manager1.get().changeClosingTime(day, clTime);
        }
    }

    public void addOpeningClosingTime(String manager,String dayName, String beginHours, String endHours){
        int opHour = Integer.parseInt(beginHours.split(":")[0]);
        int opMinutes = Integer.parseInt(beginHours.split(":")[1]);

        int clHour = Integer.parseInt(endHours.split(":")[0]);
        int clMinutes = Integer.parseInt(endHours.split(":")[1]);

        DayOfWeek day = dayFromName(dayName);

        LocalTime opTime = LocalTime.of(opHour, opMinutes);
        LocalTime clTime = LocalTime.of(clHour, clMinutes);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if(manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, opTime);
            manager1.get().changeClosingTime(day, clTime);
        }
    }

    public void managerChangeOpeningClosingTime(String manager, String dayName, String beginHours,
                                      String endHours){
        int opHour = Integer.parseInt(beginHours.split(":")[0]);
        int opMinutes = Integer.parseInt(beginHours.split(":")[1]);

        int clHour = Integer.parseInt(endHours.split(":")[0]);
        int clMinutes = Integer.parseInt(endHours.split(":")[1]);

        DayOfWeek day = dayFromName(dayName);

        LocalTime opTime = LocalTime.of(opHour, opMinutes);
        LocalTime clTime = LocalTime.of(clHour, clMinutes);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if(manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, opTime);
            manager1.get().changeClosingTime(day, clTime);
        }
    }

    public void managerChangeOpeningTime(String manager, String dayName, String beginHours){
        int opHour = Integer.parseInt(beginHours.split(":")[0]);
        int opMinutes = Integer.parseInt(beginHours.split(":")[1]);

        DayOfWeek day = dayFromName(dayName);

        LocalTime opTime = LocalTime.of(opHour, opMinutes);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if(manager1.isPresent()) {
            manager1.get().changeOpeningTime(day, opTime);
        }
    }

    public void managerChangeClosingTime(String manager, String dayName, String endHours){
        int opHour = Integer.parseInt(endHours.split(":")[0]);
        int opMinutes = Integer.parseInt(endHours.split(":")[1]);

        DayOfWeek day = dayFromName(dayName);

        LocalTime opTime = LocalTime.of(opHour, opMinutes);
        Optional<Manager> manager1 = cookieFirm.findManager(manager);

        if(manager1.isPresent()) {
            manager1.get().changeClosingTime(day, opTime);
        }
    }

    public void managerAddMonthlyCookie(String manager, String recipeName, String dough, String flavor, String topping,
                                        String mix,
                                        String cooking){
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add(toppingFromName(topping));
        Recipe recipe = new Recipe(recipeName, doughFromName(dough), flavorFromName(flavor), toppingList,
                mixFromName(mix), cookingFromName(cooking), false);

        Optional<Manager> manager1 = cookieFirm.findManager(manager);
        if(manager1.isPresent()){
            manager1.get().changeMontlyRecipe(recipe);
        }
    }


    public Integer createGuest(){
        Guest guest = new Guest();
        this.cookieFirm.addGuest(guest);
        return guest.getId();
    }

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


    public Integer createACustomer(String sName, String sLastName, String phoneNumber, String email, String
            password){
        Customer customer = this.cookieFirm.createAccount(sName, sLastName, phoneNumber, email, password);
        return customer.getId();
    }


    public boolean addACustomerToLP(String sEmail){
        Optional<Customer> customer = this.cookieFirm.findCustomer(sEmail);
        if(customer.isPresent()) {
            this.cookieFirm.addCustomerToLoyaltyProgram(customer.get());
            return true;
        }
        return false;
    }

    public void guestAddStoreToOrder(int id, String sStore){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opGuest.isPresent() && opStore.isPresent()){
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
        }
    }

    public void guestAddPickTimeToOrder(int id, int time, String pickupDay){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);

        if(opGuest.isPresent()){
            opGuest.get().getTemporaryOrder().setPickUpTime(generateTime(time, pickupDay));
        }
    }

    public void guestAddPickUpTimeAndStoreToOrder(int id, String sStore, int time, String pickupDay){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opGuest.isPresent() && opStore.isPresent()){
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(time, pickupDay));
        }
    }

    public void guestAddCookies(int id, String sStore, int quantity){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
            order.addCookie(opStore.get().getMonthlyRecipe(), quantity);
        }
    }

    public void guestRemoveCookies(int id, String sStore, int quantity){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            Order order = opGuest.get().getTemporaryOrder();
            order.setStore(opStore.get());
            order.removeCookie(opStore.get().getMonthlyRecipe(), quantity);
        }
    }

    public void guestAddSpecificCookie(int id, String sStore, int quantity, String recipeName){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            opGuest.get().getTemporaryOrder().setStore(opStore.get());
            for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
                if (cookie.getName().equals(recipeName)) {
                    opGuest.get().getTemporaryOrder().addCookie(cookie, quantity);
                    return;
                }
            }
        }
    }

    public void guestRemoveSpecificCookie(int id, String sStore, int quantity, String recipeName){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opGuest.isPresent() && opStore.isPresent()) {
            opGuest.get().getTemporaryOrder().setStore(opStore.get());
            for (Recipe cookie : cookieFirm.getGlobalRecipes()) {
                if (cookie.getName().equals(recipeName)) {
                    opGuest.get().getTemporaryOrder().removeCookie(cookie, quantity);
                    return;
                }
            }
        }
    }


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

    //TODO permettre la création de custom recipe
    /*
    public void guestOrderCustomRecipe(){

    }
    */

    //TODO permettre de refill la kitchen via le facade

    public void guestPlaceOrder(int id , Boolean payedOnline){
        Optional<Guest> opGuest = this.cookieFirm.findGuestOrCustomer(id);

        if (opGuest.isPresent()) {
            opGuest.get().placeOrder(payedOnline);
        }
    }
    public void guestModifyHisOrder(int id, String sStore, int nbCookies, boolean remove){
        Optional<Guest> guest = this.cookieFirm.findGuestOrCustomer(id);
        Optional<Store> store = this.cookieFirm.findStore(sStore);

        if(guest.isPresent() && store.isPresent()){
            if(!remove)
                guest.get().getTemporaryOrder().addCookie(store.get().getMonthlyRecipe(), nbCookies);
            else
                guest.get().getTemporaryOrder().removeCookie(store.get().getMonthlyRecipe(), nbCookies);

        }
    }


    public void guestValidateHisOrder(int id, String email, boolean pay){
        Optional<Guest> opGuest = this.cookieFirm.findGuest(id);
        if(opGuest.isPresent()) {
            opGuest.get().setEmail(email);
            opGuest.get().placeOrder(pay);
        }
    }


    public void setBankingDataCustomer(String sEmail, String accountId){
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        if(opCustomer.isPresent()) {
            BankingData bankingData = new BankingData(opCustomer.get().getFirstName(), opCustomer.get().getLastName(), accountId);
            opCustomer.get().setBankingData(bankingData);
        }

    }


    //TODO ajouter dans le bank id ajouter juste avec le accountID


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

    private LocalDateTime generateTime(int pickupTime, String pickUpDay) {
        return LocalDateTime.now().plusHours(pickupTime).with(TemporalAdjusters
                .next(DayOfWeek.valueOf(pickUpDay.toUpperCase()))).withSecond(0).withNano(0);
    }

    public DayOfWeek dayFromName(String dayName) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }

    public Mix mixFromName(String mixName) {
        for (Mix mix : cookieFirm.getCatalog().getMixList()) {
            if (mix.getName().equalsIgnoreCase(mixName)) return mix;
        }
        return null;
    }

    public Cooking cookingFromName(String cookingName) {
        for (Cooking cooking : cookieFirm.getCatalog().getCookingList()) {
            if (cooking.getName().equalsIgnoreCase(cookingName)) return cooking;
        }
        return null;
    }

    public Dough doughFromName(String doughName) {
        for (Dough dough : cookieFirm.getCatalog().getDoughList()) {
            if (dough.getName().equalsIgnoreCase(doughName)) return dough;
        }
        return null;
    }

    public Topping toppingFromName(String toppingName) {
        for (Topping topping : cookieFirm.getCatalog().getToppingList()) {
            if (topping.getName().equalsIgnoreCase(toppingName)) return topping;
        }
        return null;
    }

    public Flavor flavorFromName(String flavorName) {
        for (Flavor flavor : cookieFirm.getCatalog().getFlavorList()) {
            if (flavor.getName().equalsIgnoreCase(flavorName)) return flavor;
        }
        return null;
    }

}
