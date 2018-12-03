package store;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return freeCookie == reward.freeCookie &&
                points == reward.points;
    }

    @Override
    public int hashCode() {
        return Objects.hash(freeCookie, points);
    }
}
