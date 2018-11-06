package recipe;

public enum Mix {
    MIXED("Mixed"),
    TOPPED("Topped");

    private final String name;

    Mix(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

