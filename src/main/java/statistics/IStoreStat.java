package statistics;

import store.Store;

public interface IStoreStat<T> extends IStat<T>{

    void setStore (Store store);

}
