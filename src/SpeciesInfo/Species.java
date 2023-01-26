package SpeciesInfo;

import Model.Water;

public class Species {
    private final String species;
    private final double minWeight;
    private final double maxWeight;
    private final Water waterType;
    private final Rarity rarity; 
    private final double costPerPound;

    public Species(String species, double minWeight, double maxWeight, Water waterType, double costPerPound, Rarity rarity) {
        this.species = species;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.waterType = waterType;
        this.costPerPound = costPerPound;
        this.rarity = rarity;
    }

    public String getSpecies() {
        return this.species;
    }

    public double getMinWeight() {
        return this.minWeight;
    }

    public double getMaxWeight() {
        return this.maxWeight;
    }

    public Water getWaterType() {
        return this.waterType;
    }

    public double getCostPerPound() {
        return this.costPerPound;
    }

    public double getCost(double weight) {
        return this.costPerPound * weight;
    }

    public Rarity getRarity() {
        return this.rarity;
    }
}
