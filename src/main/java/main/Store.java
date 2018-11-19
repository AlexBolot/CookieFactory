package main;

import order.Order;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Store {

    private Recipe monthlyRecipe;
    private Collection<Recipe> globalRecipes;
    private Collection<Order> orders;
    private Map<Day, LocalDateTime> openingTimes;
    private Map<Day, LocalDateTime> closingTimes;
    private double tax;
    private Kitchen kitchen;

    public Store(Recipe monthlyRecipe, Collection<Recipe> globalRecipes, Collection<Order> orders, Map<Day, LocalDateTime> openingTimes, Map<Day, LocalDateTime> closingTimes, double tax) {
        this.monthlyRecipe = monthlyRecipe;
        this.globalRecipes = globalRecipes;
        this.orders = orders;
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
        this.tax = tax;
    }

    double placeOrder(Order order) {
        if (!this.checkOrderValidity(order)) {
            throw new IllegalArgumentException("The order is not valid");
        } else {
            orders.add(order);
            return order.placeOrder();
        }
    }

    public void cancelOrder(Order order) {
        order.cancel();
    }

    /**
     * Checks if the order is valid : Order contains cookies + PickupTime is valid
     *
     * @param order Customer's order to check before sending the order
     * @return True if the order is valid, false otherwise
     */
    boolean checkOrderValidity(Order order) {
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
     * Set the order to payed
     *
     * @param day        to pick up the order
     * @param pickUpTime time to pick up the order
     * @param email      the current customer
     */
    void setStatusPaymentOrder(Day day, LocalDateTime pickUpTime, String email) {
        Optional<Order> order = findOrder(pickUpTime, day, email);

        order.ifPresent(Order::setPayed);
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

    // region --------------- Getters and Setters ---------------

    /**
     * Builds a list of Recipe containing the Global ones and the monthly of this store
     *
     * @return The list of all available Recipes at this Store
     */
    Collection<Recipe> getRecipes() {
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

    public Collection<Order> getOrders() {
        return orders;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    /**
     * Sets the new Monthly Recipe of the store
     * Raise an exception if new ingredient is the same as the previous one
     *
     * @param newRecipe new monthly ingredient of the store
     */
    void setMonthlyRecipe(Recipe newRecipe) {
        if (this.monthlyRecipe != null && this.monthlyRecipe == newRecipe) {
            throw new IllegalArgumentException("Recipe " + newRecipe + " is the same as the previous one");
        }

        this.monthlyRecipe = newRecipe;
    }

    public void setOpeningTime(Day day, LocalDateTime localDateTime) {
        // Check if store has a closing time for the [day], to ensure no time crossing
        if (closingTimes.containsKey(day) && localDateTime.isAfter(closingTimes.get(day)))
            throw new IllegalArgumentException("Trying to set opening time after closing time for " + day);

        this.openingTimes.put(day, localDateTime);
    }

    public void setClosingTime(Day day, LocalDateTime localDateTime) {
        // Check if store has a opening time for the [day], to ensure no time crossing
        if (openingTimes.containsKey(day) && localDateTime.isBefore(openingTimes.get(day)))
            throw new IllegalArgumentException("Trying to set closing time before opening time for " + day);

        this.closingTimes.put(day, localDateTime);
    }

}