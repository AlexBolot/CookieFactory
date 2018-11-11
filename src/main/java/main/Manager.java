package main;

import java.time.LocalDateTime;

public class Manager {

    private Store store;

    public Manager(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void changeOpeningTime(Day day, LocalDateTime time) {
        store.setOpeningTime(day, time);
    }

    public void changeClosingTime(Day day, LocalDateTime time) {
        store.setClosingTimes(day, time);
    }

    public void changeMontlyRecipe(Recipe recipe) {
        store.setMonthlyRecipe(recipe);
    }

}