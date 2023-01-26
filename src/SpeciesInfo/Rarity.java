package SpeciesInfo;

public enum Rarity {
    COMMON(1),
    UNCOMMON(5),
    RARE(10),
    EXOTIC(50),
    LEGENDARY(100),
    MYTHIC(500);

    public int value;

    private Rarity(int value) {
        this.value = value;
    }
}
