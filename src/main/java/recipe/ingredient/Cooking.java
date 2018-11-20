package recipe.ingredient;

import java.util.Objects;

public class Cooking {

    private final String name;

    Cooking(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cooking cooking = (Cooking) o;
        return Objects.equals(name, cooking.name);
    }
}