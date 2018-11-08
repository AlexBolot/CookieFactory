package ingredient;

public enum Cooking {
    CRUNCHY("Crunchy"),
    CHEWY("Chewy");

    private final String name;

    Cooking(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}