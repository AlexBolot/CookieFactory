package store;

import java.util.Objects;

public class Reward {
    private boolean freeCookie;
    private int value;

    public Reward(boolean freeCookie, int value) {
        this.freeCookie = freeCookie;
        this.value = value;
    }

    public boolean hasFreeCookie() {
        return freeCookie;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return freeCookie == reward.freeCookie &&
                value == reward.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(freeCookie, value);
    }
}
