package main;

import order.Order;
import order.OrderState;
import store.Store;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Optional;

public class Facade {

    private CookieFirm cookieFirm;

    public Facade(CookieFirm cookieFirm){
        this.cookieFirm = cookieFirm;
    }

    /*
    public addAStoreToFirm(String sStore){
        Store store = new Store();
        cookieFirm.addStore(store);
    }*/


    public void createACustomer(String sName, String sLastName, String phoneNumber, String email, String
            password){
        this.cookieFirm.createAccount(sName, sLastName, phoneNumber, email, password);
    }


    public boolean addACustomerToLP(String sEmail){
        Optional<Customer> customer = this.cookieFirm.findCustomer(sEmail);
        if(customer.isPresent()) {
            this.cookieFirm.addCustomerToLoyaltyProgram(customer.get());
            return true;
        }
        return false;
    }

    public void customerAddStoreToOrder(String sEmail, String sStore){
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opCustomer.isPresent() && opStore.isPresent()){
            Order order = opCustomer.get().getTemporaryOrder();
            order.setStore(opStore.get());
        }
    }

    public void customerAddPickTimeToOrder(String sEmail, int time, String pickupDay){
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);

        if(opCustomer.isPresent()){
            opCustomer.get().getTemporaryOrder().setPickUpTime(generateTime(time, pickupDay));
        }
    }

    public void customerPlaceOrderWithCookies(String sEmail, String sStore, int nbCookies, int
            pickupTime, String pickUpDay, Boolean payedOnline) {
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if (opCustomer.isPresent() && opStore.isPresent()) {
            Order order = opCustomer.get().getTemporaryOrder();
            order.addCookie(opStore.get().getMonthlyRecipe(), nbCookies);
            order.setStore(opStore.get());
            order.setPickUpTime(generateTime(pickupTime, pickUpDay));

            opCustomer.get().placeOrder(payedOnline);
        }
    }

    public void customerModifyHisOrder(String sEmail, String sStore, int nbCookies, boolean remove){
        Optional<Customer> customer = this.cookieFirm.findCustomer(sEmail);
        Optional<Store> store = this.cookieFirm.findStore(sStore);

        if(customer.isPresent() && store.isPresent()){
            if(!remove)
                customer.get().getTemporaryOrder().addCookie(store.get().getMonthlyRecipe(), nbCookies);
            else
                customer.get().getTemporaryOrder().removeCookie(store.get().getMonthlyRecipe(), nbCookies);

        }
    }

    /*
    public void guestValidateHisOrder(String sEmail){

    }*/



    public boolean anEmployeeMakeAnActionOnOrder(String sStore, int time, String day, String email, String action){
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opStore.isPresent()){
            Optional<Order> opOrder = opStore.get().findOrder(generateTime(time, day), email);

            if(opOrder.isPresent()) {
                if(action.equals("cancel")) {
                    opOrder.get().cancel();
                    return true;
                }
                else if(action.equals("withdrawn")){
                    opOrder.get().withdraw();
                    return true;
                }
            }
        }
        return false;
    }


    private LocalDateTime generateTime(int pickupTime, String pickUpDay) {
        return LocalDateTime.now().plusHours(pickupTime).with(TemporalAdjusters
                .next(DayOfWeek.valueOf(pickUpDay.toUpperCase())));
    }
}
