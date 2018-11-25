package main;

import order.Order;
import order.OrderState;
import store.Store;

import java.time.LocalDateTime;
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


    public void addCustomerToFirm(String sName, String sLastName, String phoneNumber, String email, String password){
        this.cookieFirm.createAccount(sName, sLastName, phoneNumber, email, password);
    }


    public void customerOrderCookies(String sEmail, String sStore, int nbCookies, int pickupTime, String pickUpDay,
                                     Boolean payedOnline){
        Optional<Customer> opCustomer = this.cookieFirm.findCustomer(sEmail);
        Optional<Store> opStore = this.cookieFirm.findStore(sStore);

        if(opCustomer.isPresent() && opStore.isPresent()){
            Customer customer = opCustomer.get();
            Store store = opStore.get();
            Order order = new Order(store, LocalDateTime.now().plusHours(pickupTime), Day.dayFromName(pickUpDay));
            order.addCookie(store.getMonthlyRecipe(), nbCookies);
            customer.setTemporaryOrder(order);
            customer.placeOrder(payedOnline);
        }
    }




}
