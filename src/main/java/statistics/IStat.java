package statistics;

public interface IStat<T> {

    T computeValue();
    void calculate();
    void cleanUp();
    String serialize();
    T getStat();
}
