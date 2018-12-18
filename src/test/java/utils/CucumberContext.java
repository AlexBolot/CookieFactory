package utils;

import main.CookieFirm;
import main.Customer;
import main.Facade;
import main.Guest;
import order.Order;
import store.Manager;
import store.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CucumberContext {

    public final Map<String, Manager> managers = new HashMap<>();
    public final Map<String, Store> stores = new HashMap<>();
    private final Map<String, Guest> clients = new HashMap<>();
    public final Map<String, Order> orders = new HashMap<>();
    public final TestUtils utils = new TestUtils();
    private Integer currentId;
    private Exception exception;

    private final Facade facade = new Facade();

    private static CucumberContext context;


    public static CucumberContext getContext() {
        if (context == null) context = new CucumberContext();
        return context;
    }

    public Integer getCurrentId() {
        return this.currentId;
    }

    public void setCurrentId(Integer id) {
        this.currentId = id;
    }

    public void addGuest(String name, Guest guest) {
        clients.put(name, guest);
    }

    public Guest getGuest(String guestName) {
        return clients.get(guestName);
    }

    public Customer getCustomer(String customerName) {
        return (Customer) clients.get(customerName);
    }

    public Order getOrder(String orderName) {
        return orders.get(orderName);
    }

    public Store getStore(String storeName) {
        return stores.get(storeName);
    }

    public Facade getFacade() {
        return facade;
    }

    public CookieFirm cookieFirm() {
        return CookieFirm.instance();
    }

    public void pushException(Exception e) {
        this.exception = e;
    }

    public Optional<Exception> popException() {
        Optional<Exception> res = Optional.ofNullable(exception);
        exception = null;

        return res;
    }
}
