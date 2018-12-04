package store;

import recipe.Recipe;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Manager {

    private Store store;
    private String name;

    public Manager(Store store, String name) {
        this.store = store;
        this.name = name;
    }

    public void changeOpeningTime(DayOfWeek day, LocalTime time) {
        store.setOpeningTime(day, time);
    }

    public void changeClosingTime(DayOfWeek day, LocalTime time) {
        store.setClosingTime(day, time);
    }

    public void changeMontlyRecipe(Recipe recipe) {
        store.setMonthlyRecipe(recipe);
    }

    public Store getStore() {
        return store;
    }

    public String getName(){return name;}


}