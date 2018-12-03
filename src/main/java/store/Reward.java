package store;

public class Reward {
    boolean freeCookie;
    int points;

    public Reward(boolean freeCookie, int points) {
        this.freeCookie = freeCookie;
        this.points = points;
    }

    public boolean hasFreeCookie() {
        return freeCookie;
    }

    public int getPoints() {
        return points;
    }
}
