package statistics;

public abstract class Statistic<T> {
    T value;

    public T computeValue() {
        calculate();
        cleanUp();
        return value;
    }

    abstract void calculate();

    abstract void cleanUp();

    public T getStat() {return value;}
}