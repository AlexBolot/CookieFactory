package statistics;

public abstract class Statistic<T> implements IStat<T>{
    T value;

    public T computeValue() {
        calculate();
        cleanUp();
        return value;
    }

    public T getStat() {return value;}
}