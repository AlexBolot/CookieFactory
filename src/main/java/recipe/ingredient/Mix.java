package recipe.ingredient;

import java.util.Objects;

public class Mix {

    private final String name;

    Mix(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mix mix = (Mix) o;
        return Objects.equals(name, mix.name);
    }
}

