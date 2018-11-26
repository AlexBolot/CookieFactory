package store;

import main.Day;
import recipe.Recipe;

import java.time.LocalTime;

public class Manager {

    private Store store;

    public Manager(Store store) {
        this.store = store;
    }

    public void changeOpeningTime(Day day, LocalTime time) {
        store.setOpeningTime(day, time);
    }

    public void changeClosingTime(Day day, LocalTime time) {
        store.setClosingTime(day, time);
    }

    public void changeMontlyRecipe(Recipe recipe) {
        store.setMonthlyRecipe(recipe);
    }

    public Store getStore() {
        return store;
    }


}