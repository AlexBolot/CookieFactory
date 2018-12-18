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
