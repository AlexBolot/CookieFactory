package main;

import order.Order;

import java.time.LocalDateTime;
import java.util.*;

public class Store {

    private Recipe monthlyRecipe;
    private Collection<Recipe> globalRecipes;
    private Collection<Order> orders;
    private Map<Day, LocalDateTime> openingTimes;
    private Map<Day, LocalDateTime> closingTimes;
    private double tax;

    public Store(double tax) {
        this.tax = tax;

        globalRecipes = new ArrayList<>();
        orders = new ArrayList<>();

        openingTimes = new HashMap<>();
        closingTimes = new HashMap<>();
    }

    public Store(Recipe monthlyRecipe, Collection<Recipe> globalRecipes, Collection<Order> orders, Map<Day, LocalDateTime> openingTimes, Map<Day, LocalDateTime> closingTimes, double tax) {
        this.monthlyRecipe = monthlyRecipe;
        this.globalRecipes = globalRecipes;
        this.orders = orders;
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
        this.tax = tax;
    }

    /**
     * Builds a list of Recipe containing the Global ones and the monthly of this store
     *
     * @return The list of all available Recipes at this Store
     */
    public Collection<Recipe> getRecipes() {
        Collection<Recipe> recipes = globalRecipes;
        recipes.add(monthlyRecipe);
        return recipes;
    }

    public Map<Day, LocalDateTime> openingTimes() {
        return openingTimes;
    }

    public LocalDateTime openingTime(Day day) {
        return openingTimes.get(day);
    }

    public Map<Day, LocalDateTime> closingTimes() {
        return closingTimes;
    }

    public LocalDateTime closingTime(Day day) {
        return closingTimes.get(day);
    }

    public double getTax() {
        return tax;
    }

    public Recipe getMonthlyRecipe() {
        return monthlyRecipe;
    }

    public void setOpeningTime(Day day, LocalDateTime localDateTime) {
        this.openingTimes.remove(day, localDateTime);
    }

    public void setClosingTimes(Day day, LocalDateTime localDateTime) {
        this.closingTimes.remove(day, localDateTime);
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public boolean placeOrder(Order order) {
        if (!this.checkOrderValidity(order)) {
            return false;
        } else {
            orders.add(order);
        }
        return true;
    }

    /**
     * Sets the new Monthly Recipe of the store
     * Raise an exception if new ingredient is the same as the previous one
     *
     * @param newRecipe new monthly ingredient of the store
     */
    public void setMonthlyRecipe(Recipe newRecipe) {
        if (this.monthlyRecipe != null && this.monthlyRecipe == newRecipe) {
            throw new IllegalArgumentException("Recipe " + newRecipe + " is the same as the previous one");
        }

        this.monthlyRecipe = newRecipe;
    }

    /**
     * Checks if the order is valid : Order contains cookies + PickupTime is valid
     *
     * @param order Customer's order to check before sending the order
     * @return True if the order is valid, false otherwise
     */
    public boolean checkOrderValidity(Order order) {
        return checkOrderContent(order) && checkOrderDelay(order);
    }

    /**
     * Checks that the order is not empty
     *
     * @param order Customer's order that needs checking
     * @return True is the order has oreder lines, false if order is empty
     */
    private boolean checkOrderContent(Order order) {
        return !order.getOrderLines().isEmpty();
    }

    /**
     * Checks if the pickup time constraints are granted
     *
     * @param order Customer's order that needs checking
     * @return True is the order respectes the time constraints, false otherwise
     */
    @SuppressWarnings("RedundantIfStatement")
    private boolean checkOrderDelay(Order order) {
        Day pickupDay = order.getPickupDay();
        LocalDateTime pickupTime = order.getPickUpTime();


        // Selected time is in less than two hours -> forbidden
        if (LocalDateTime.now().compareTo(pickupTime.minusHours(2)) > 0)
            return false;

        // Selected time is earlier than opening time of selected day -> forbidden
        if (pickupTime.compareTo(this.openingTime(pickupDay)) < 0)
            return false;

        // Selected time is later than closing time of selected day -> forbidden
        if (pickupTime.compareTo(this.closingTime(pickupDay)) > 0)
            return false;

        // Else -> no problem :)
        return true;
    }

    /**
     * Tries to find an order matching the passed values
     *
     * @param pickUpTime {@link LocalDateTime } the required pick up time
     * @param pickupDay  {@link Day} the required day
     * @param email      {@link String} the guest email
     * @return {@link Optional<Order>}Optional containing the found order if exists
     */
    public Optional<Order> findOrder(LocalDateTime pickUpTime, Day pickupDay, String email) {
        return orders.stream()
                .filter(order ->
                        pickupDay.equals(order.getPickupDay()) &&
                                pickUpTime.equals(order.getPickUpTime()) &&
                                email.equals(order.getGuest().getEmail())
                )
                .findFirst();
    }
}