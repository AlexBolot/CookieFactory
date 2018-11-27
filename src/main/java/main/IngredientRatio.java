package main;

import recipe.ingredient.*;

import java.util.HashMap;
import java.util.Map;

public class IngredientRatio {
    private Map<Dough,Double> doughRatio;
    private Map<Flavor,Double> flavorRatio;
    private Map<Topping,Double> toppingRatio;
    private Map<Mix,Double> mixRatio;
    private Map<Cooking,Double> cookingRatio;

    public IngredientRatio() {
        this.doughRatio = new HashMap<>();
        this.flavorRatio = new HashMap<>();
        this.toppingRatio = new HashMap<>();
        this.mixRatio = new HashMap<>();
        this.cookingRatio = new HashMap<>();
    }

    public void addDough(Dough dough, double amount) {
        doughRatio.put(dough,doughRatio.containsKey(dough)?doughRatio.get(dough)+amount:amount);
    }
    public void addFlavor(Flavor flavor, double amount) {
        flavorRatio.put(flavor,flavorRatio.containsKey(flavor)?flavorRatio.get(flavor)+amount:amount);
    }
    public void addTopping(Topping topping, double amount) {
        toppingRatio.put(topping,toppingRatio.containsKey(topping)?toppingRatio.get(topping)+amount:amount);
    }
    public void addMix(Mix mix, double amount) {
        mixRatio.put(mix,mixRatio.containsKey(mix)?mixRatio.get(mix)+amount:amount);
    }
    public void addCooking(Cooking cooking, double amount) {
        cookingRatio.put(cooking,cookingRatio.containsKey(cooking)?cookingRatio.get(cooking)+amount:amount);
    }

    public void normalizeDough() {
        double sum= doughRatio.values().stream().mapToDouble(value -> value).sum();
        doughRatio.keySet().forEach(dough -> doughRatio.put(dough, doughRatio.get(dough) / sum));
    }

    public void normalizeFlavor() {
        double sum= flavorRatio.values().stream().mapToDouble(value -> value).sum();
        flavorRatio.keySet().forEach(flavor -> flavorRatio.put(flavor, flavorRatio.get(flavor) / sum));
    }

    public void normalizeTopping() {
        double sum= toppingRatio.values().stream().mapToDouble(value -> value).sum();
        toppingRatio.keySet().forEach(topping -> toppingRatio.put(topping, toppingRatio.get(topping) / sum));
    }

    public void normalizeMix() {
        double sum= mixRatio.values().stream().mapToDouble(value -> value).sum();
        mixRatio.keySet().forEach(mix -> mixRatio.put(mix, mixRatio.get(mix) / sum));
    }

    public void normalizeCooking() {
        double sum= cookingRatio.values().stream().mapToDouble(value -> value).sum();
        cookingRatio.keySet().forEach(cooking -> cookingRatio.put(cooking, cookingRatio.get(cooking) / sum));
    }

    public void normalizeAll() {
        normalizeDough();
        normalizeFlavor();
        normalizeTopping();
        normalizeMix();
        normalizeCooking();
    }

    public Map<Dough, Double> getDoughRatio() {
        return doughRatio;
    }

    public Map<Flavor, Double> getFlavorRatio() {
        return flavorRatio;
    }

    public Map<Topping, Double> getToppingRatio() {
        return toppingRatio;
    }

    public Map<Mix, Double> getMixRatio() {
        return mixRatio;
    }

    public Map<Cooking, Double> getCookingRatio() {
        return cookingRatio;
    }
}
