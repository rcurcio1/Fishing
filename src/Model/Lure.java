package Model;

public enum Lure {
    NO_LURE(0, "No Lure", 0), 
    FASTER_BITES1(2, "Faster Bites I", 75), 
    RARER_FISH1(2, "Rarer Fish I", 125),
    BIGGER_FISH1(2, "Bigger Fish I", 75),
    FASTERBITES2(5, "Faster Bites II", 150),
    RARER_FISH2(5, "Rare Fish II", 250),
    BIGGER_FISH2(5, "Bigger Fish II", 150),
    FASTERBITES3(15,"Faster Bites III", 750),
    RARER_FISH3(15, "Rarer Fish III", 1250),
    BIGGER_FISH3(15, "Bigger Fish III", 750);

    private int rodLevelUnlocked;
    private String name;
    private int price;

    private Lure(int rodLevelUnlocked, String name, int price) {
        this.rodLevelUnlocked = rodLevelUnlocked;
        this.name = name;
        this.price = price;
    }

    public int getRodLevelUnlocked() {
        return this.rodLevelUnlocked;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }
}
