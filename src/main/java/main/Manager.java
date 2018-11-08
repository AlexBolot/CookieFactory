package main;

import recipe.Recipe;

import java.time.LocalDateTime;

public class Manager {

    private Store store;

    public Manager(Store store) {
        this.store = store;
    }

    public void changeOpeningTime(LocalDateTime time, Day day) {
        store.setOpeningTime(day, time);
    }

    public void changeClosingTime(LocalDateTime time, Day day) {
        store.setClosingTimes(day, time);
    }

    public void changeMontlyRecipe(Recipe recipe) {
        store.setMonthlyRecipe(recipe);
    }

}