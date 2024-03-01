package SpeciesInfo;

public enum Rarity {
    COMMON(1),
    UNCOMMON(2),
    RARE(4),
    EXOTIC(8),
    LEGENDARY(16),
    MYTHIC(32);

    public int value;

    private Rarity(int value) {
        this.value = value;
    }
}
