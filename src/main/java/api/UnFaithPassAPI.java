package api;

import java.util.Objects;

public class UnFaithPassAPI {
    private final String lastname;
    private final String firstname;
    private int points;
    private int freeCookies;

    public UnFaithPassAPI(String lastname, String firstname) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.points = 0;
        this.freeCookies = 0;
    }

    /**
     * Add to the UnFaithPass an amount of points and free Cookie usable
     *
     * @param earnedPoints , number of point won with a purchase
     * @param earnedFreeCookies , number of free cookies won with a purchase
     */
    public void win(int earnedPoints, int earnedFreeCookies) {
        this.points += earnedPoints;
        this.freeCookies += earnedFreeCookies;
    }

    /**
     * Update of points and free cookies with a payment with the UnFaithPass
     *
     * @param usedPoints , points used for a payment
     * @param usedFreeCookies , number of free cookies asked
     */
    public boolean use(int usedPoints, int usedFreeCookies) {
        if (usedPoints > points || usedFreeCookies > freeCookies) {
            return false;
        }
        this.points -= usedPoints;
        this.freeCookies -= usedFreeCookies;
        return true;
    }

    public int getPoints() {
        return points;
    }

    public int getFreeCookies() {
        return freeCookies;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setFreeCookies(int freeCookies) {
        this.freeCookies = freeCookies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnFaithPassAPI that = (UnFaithPassAPI) o;
        return points == that.points &&
                freeCookies == that.freeCookies &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(firstname, that.firstname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastname, firstname, points, freeCookies);
    }
}
