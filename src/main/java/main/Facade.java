package main;

import api.BankingData;
import order.Order;
import store.Store;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
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

    public void addOpeningClosingTime(String sStore, String dayName, int behindHours, int aheadHours){
        DayOfWeek day = dayFromName(dayName);
        LocalTime opTime = LocalTime.now().minusHours(behindHours);
        LocalTime clTime = LocalTime.now().plusHours(aheadHours);

        Optional<Store> store = cookieFirm.findStore(sStore);

        if(store.isPresent()) {
            store.get().setOpeningTime(day, opTime);
            store.get().setClosingTime(day, clTime);
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
                if(action.toUpperCase().equals("CANCEL")) {
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

}
