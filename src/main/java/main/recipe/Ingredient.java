package main.recipe;

public abstract class Ingredient  {

    private String name;

    Ingredient(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}