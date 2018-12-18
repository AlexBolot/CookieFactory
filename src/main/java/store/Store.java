package store;


import main.CookieFirm;
import order.Order;
import order.OrderLine;
import recipe.Recipe;
import recipe.ingredient.Ingredient;

import java.time.*;
import java.util.*;

public class Store {

    private String name;
    private Recipe monthlyRecipe;
    private Collection<Recipe> globalRecipes;
    private Collection<Order> orders = new ArrayList<>();
    private Map<DayOfWeek, LocalTime> openingTimes;
    private Map<DayOfWeek, LocalTime> closingTimes;
    private double tax;
    private double customRecipeeMargin;
    private Kitchen kitchen;
    private UnFaithPass unFaithPass;

    public Store(String name,
                 Recipe monthlyRecipe,
                 Collection<Recipe> globalRecipes,
                 Map<DayOfWeek, LocalTime> openingTimes,
                 Map<DayOfWeek, LocalTime> closingTimes,
                 double tax, double customeRecipeeMargin) {
        this.name = name;
        this.monthlyRecipe = monthlyRecipe;
        this.globalRecipes = globalRecipes;
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
        this.tax = tax;
        this.customRecipeeMargin = customeRecipeeMargin;
    }

    public Store(String name, double tax, double customeRecipeeMargin) {
        this.name = name;
        this.monthlyRecipe = null;
        this.globalRecipes = new ArrayList<>();
        this.openingTimes = new HashMap<>();
        this.closingTimes = new HashMap<>();
        this.tax = tax;
        this.customRecipeeMargin = customeRecipeeMargin;
    }

    public double placeOrder(Order order) {

        // Throws exception if order not valid
        this.checkOrderValidity(order);

        orders.add(order);

        for (OrderLine orderLine : order.getOrderLines()) {
            kitchen.cook(orderLine.getRecipe(), orderLine.getAmount());
        }

        return order.placeOrder();
    }

    /**
     * Provides a stack of all the rewards corresponding to a given order and the unFaithPath applied to the store
     *
     * @param order scanned for rewards
     * @return a list of reward for all cookies in the order
     */
    public List<Reward> getRewards(Order order) {
        if (this.unFaithPass == null) {
            throw new IllegalStateException("Trying to check for rewards without unFaithPass applied");
        }
        List<Reward> rewards = new ArrayList<>();
        for (OrderLine orderline : order.getOrderLines()) {
            Reward reward = unFaithPass.getRewardFromRecipe(orderline.getRecipe());
            if (reward != null) {
                rewards.addAll(Collections.nCopies(orderline.getAmount(), reward));
            }
        }
        return rewards;
    }

    /**
     * Require the given order to cancel itself, and then, for each cookie, and for each ingredient, refills the kitchen
     * (since the order was not withdrawn, the ingredients were booked but not baked :p)
     *
     * @param order Order to cancel
     */
    public void cancelOrder(Order order) {

        order.cancel();

        for (OrderLine orderLine : order.getOrderLines()) {
            for (Ingredient ingredient : orderLine.getRecipe().getIngredients()) {
                kitchen.refill(ingredient, orderLine.getAmount());
            }
        }
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
        for (OrderLine orderLine : order.getOrderLines()) {
            if (kitchen.recipeCapacity(orderLine.getRecipe()) < orderLine.getAmount())
                throw new IllegalArgumentException("The store's kitchen is unable to cook a recipe of the order");
        }

        if (order.getOrderLines().isEmpty()) {
            throw new IllegalArgumentException("The order given is empty !");
        }

        return true;
    }

    /**
     * Checks if the pickup time constraints are granted
     *
     * @param order Customer's order that needs checking
     * @return True is the order respectes the time constraints, false otherwise
     */
    private boolean checkOrderDelay(Order order) {

        LocalDateTime pickUpDateTime = order.getPickUpTime();

        Clock clock = CookieFirm.instance().getClock();
        LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault()).withSecond(0).withNano(0);

        LocalTime pickupTime = LocalTime.from(pickUpDateTime);
        DayOfWeek pickupDay = pickUpDateTime.getDayOfWeek();

        // Selected time is earlier than opening time of selected day -> forbidden
        if (pickupTime.isBefore(LocalTime.from(this.openingTime(pickupDay))))
            throw new IllegalArgumentException("The pickup time is earlier than opening time of the store");

        // Selected time is later than closing time of selected day -> forbidden
        if (pickupTime.isAfter(LocalTime.from(this.closingTime(pickupDay))))
            throw new IllegalArgumentException("The pickup time is later than closing time of the store");

        // Selected time is in less than two hours -> forbidden
        if (pickUpDateTime.isBefore(now.plusHours(2)))
            throw new IllegalArgumentException("The pickup time is in less than 2h");


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
    void setStatusPaymentOrder(DayOfWeek day, LocalDateTime pickUpTime, String email) {

        //TODO : Remove since no more payment

        //Optional<Order> order = findOrder(pickUpTime, email);
        //order.ifPresent(Order::setPayed);
    }

    /**
     * Tries to find an order matching the passed values
     *
     * @param pickUpTime {@link LocalDateTime } the required pick up time
     * @param email      {@link String} the guest email
     * @return {@link Optional<Order>}Optional containing the found order if exists
     */
    public Optional<Order> findOrder(LocalDateTime pickUpTime, String email) {
        return orders.stream()
                .filter(order -> pickUpTime.isEqual(order.getPickUpTime()) && email.equals(order.getGuest().getEmail()))
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

    public Map<DayOfWeek, LocalTime> openingTimes() {
        return openingTimes;
    }

    public LocalTime openingTime(DayOfWeek day) {
        return openingTimes.get(day);
    }

    public Map<DayOfWeek, LocalTime> closingTimes() {
        return closingTimes;
    }

    public LocalTime closingTime(DayOfWeek day) {
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

    public String getName() {
        return name;
    }

    public UnFaithPass getUnFaithPass() {
        return this.unFaithPass;
    }

    /**
     * Sets the new Monthly Recipe of the store
     * Raise an exception if new recipe.ingredient is the same as the previous one
     *
     * @param newRecipe new monthly recipe.ingredient of the store
     */
    public void setMonthlyRecipe(Recipe newRecipe) {
        if (this.monthlyRecipe != null && this.monthlyRecipe == newRecipe)
            throw new IllegalArgumentException("Recipe " + newRecipe + " is the same as the previous one");

        this.monthlyRecipe = newRecipe;
    }

    void setOpeningTime(DayOfWeek day, LocalTime localTime) {
        // Check if store has a closing time for the [day], to ensure no time crossing
        if (closingTimes.containsKey(day) && localTime.isAfter(closingTimes.get(day)))
            throw new IllegalArgumentException("Trying to set opening time after closing time for " + day);

        openingTimes.put(day, localTime);
    }

    void setClosingTime(DayOfWeek day, LocalTime localTime) {
        // Check if store has a opening time for the [day], to ensure no time crossing
        if (openingTimes.containsKey(day) && localTime.isBefore(openingTimes.get(day)))
            throw new IllegalArgumentException("Trying to set closing time before opening time for " + day);

        closingTimes.put(day, localTime);
    }

    public void applyUnFaithPath(UnFaithPass unFaithPass) {
        this.unFaithPass = unFaithPass;
    }

    public double getRecipePrice(Recipe recipe) {
        return recipe.getIngredients().stream().mapToDouble(kitchen::vendingPriceOf).sum() + (recipe.isCustom() ? customRecipeeMargin : 0.0);
    }

    public double getCustomRecipeeMargin() {
        return customRecipeeMargin;
    }

    public void setCustomRecipeeMargin(double customRecipeeMargin) {
        this.customRecipeeMargin = customRecipeeMargin;
    }
}