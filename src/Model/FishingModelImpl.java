package Model;

import SpeciesInfo.FishLocations;
import SpeciesInfo.Species;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FishingModelImpl implements FishingModel {
  private int money;
  private String location;
  private int rodLevel;
  private List<Fish> inventory;
  private Water waterType;
  private FishLocations fishLocations;
  private Lure lure;
  private int lureDuration;
  private Map<String, Map<Water, Map<String, Boolean>>> almanac;

  public FishingModelImpl() {
    this.money = 0;
    this.location = "Newtown Square";
    this.rodLevel = 1;
    this.inventory = new ArrayList<Fish>();
    this.waterType = Water.CREEK;
    this.fishLocations = new FishLocations();
    this.almanac = this.fishLocations.makeAlmanac();
    this.lure = Lure.NO_LURE;
    this.lureDuration = 0;
  }

  public FishingModelImpl(int money, String location, int rodLevel, ArrayList<Fish> inventory,
      Water waterType) {
    this.money = money;
    this.location = location;
    this.rodLevel = rodLevel;
    this.inventory = inventory;
    this.waterType = waterType;
  }

  public FishingModelImpl(FishingModelImpl model) {
    this.money = model.getMoney();
    this.location = model.getLocation();
    this.rodLevel = model.getRodLevel();
    this.inventory = model.getInventory();
    this.waterType = model.getWaterType();
    this.fishLocations = model.getFishLocation();
    this.almanac = model.getAlmanac();
    this.lure = model.getLure();
    this.lureDuration = model.getLureDuration();
  }

  @Override
  public void addFishToInventory(Fish f) {
    this.inventory.add(f);
  }

  @Override
  public void changeLocation(String location, Water w) {
    this.location = location;
    this.waterType = w;
  }

  @Override
  public void upgradeRod() {
    this.rodLevel ++;
  }

  @Override
  public void changeMoney(int d) {
    this.money += d;
  }

  @Override
  public int getMoney() {
    return this.money;
  }

  @Override
  public List<Fish> getInventory() {
    return this.inventory;
  }

  @Override
  public void removeFishFromInventory(int i) {
    this.inventory.remove(i);
  }

  @Override
  public int getRodLevel() {
    return this.rodLevel;
  }

  @Override
  public String getLocation() {
    return this.location;
  }

  @Override
  public Water getWaterType() {
    return this.waterType;
  }

  @Override
  public Boolean isGameOver() {
    return false;
  }

  public FishLocations getFishLocation() {
    return this.fishLocations;
  }

  @Override
  public Species getRandomFishByLocation() {
    List<Species> options = new ArrayList<Species>();
    for (Species s: this.fishLocations.getFishLocations().get(this.location)) {
      if (s.getWaterType() == this.waterType) {
        options.add(s);
      }
    }
    return this.getRandomFish(options);
  }

  private Species getRandomFish(List<Species> options) {
    Random rand = new Random();
    int i = rand.nextInt(options.size());
    Species s = options.get(i);
    if (rand.nextInt((s.getRarity().value / this.getRarityDivider())) == 0) {
      return s;
    }
    else {
      return getRandomFish(options);
    }
  }

  private int getRarityDivider() {
    switch (this.lure) {
      case RARER_FISH1:
        return 2;
      case RARER_FISH2:
        return 4;
      case RARER_FISH3:
        return 8;
      default:
        return 1;
    }
  }

  @Override
  public int getInventoryCost() {
    int cost = 0;
    for (Fish f: this.inventory) {
      cost += f.getWorth();
    }
    return cost;
  }

  @Override
  public void emptyInventory() {
    this.inventory = new ArrayList<Fish>();    
  }

  @Override
  public Map<String, List<Species>> getFishLocations() {
    return this.fishLocations.getFishLocations();
  }

  @Override
  public void setLocation(String l) {
    this.location = l;
  }

  @Override
  public List<Water> getWaterLocations(String location) {
    return this.fishLocations.getWaterLocations().get(location);
  }

  @Override
  public void setWater(String s) {
    this.waterType = Water.valueOf(s);    
  }

  @Override
  public Fish catchFish() {
    try {
      Thread.sleep(this.getWaitTime());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Species s = this.getRandomFishByLocation();
    double weight = this.calculatwWeight(s);
    int cost = (int) Math.round(s.getCost(weight));
    Fish f = new Fish(s.getSpecies(), weight, cost);
    this.updateLure();
    this.updateAlmanac(f.getName());
    return f;
  }

  private double calculatwWeight(Species s) {
    double difference = s.getMaxWeight() - s.getMinWeight();
    double base = s.getMinWeight() + (difference * this.rodLevel / 100);
    switch(this.lure) {
      case BIGGER_FISH1:
        return base * 1.25;
      case BIGGER_FISH2:
        return base * 1.5;
      case BIGGER_FISH3:
        return base * 2;
      default:
        return base;
    }
  }

  private void updateLure() {
    if (this.lureDuration > 0) {
      this.lureDuration--;
    }
    if (this.lureDuration == 0) {
      this.lure = Lure.NO_LURE;
    }
  }

  private void updateAlmanac(String name) {
    this.almanac.get(this.location).get(this.waterType).put(name, true);
  }

  private long getWaitTime() {
    int base = 5000 - (this.rodLevel * 50);
    //Random rand = new Random(); TODO: re-add randomization of wait times
    //base -= rand.nextInt(100);
    switch (this.lure) {
      case FASTER_BITES1:
        return Math.round(base * .75);
      case FASTERBITES2:
        return Math.round(base * .5);
      case FASTERBITES3:
        return Math.round(base * .25);
      default:
        return base;
    }
  }

  @Override
  public Lure getLure() {
    return this.lure;
  }

  @Override
  public int getLureDuration() {
    return this.lureDuration;
  }

  @Override
  public Map<String, Map<Water, Map<String, Boolean>>> getAlmanac() {
    return this.almanac;
  }

  @Override
  public void setLure(Lure lure, int duration) {
    if (this.lure.equals(lure)) {
      this.lureDuration += duration;
    }
    else {
      this.lureDuration = duration;
    }
    this.lure = lure;
  }

  @Override
  public List<Lure> getActiveLureOfferings() {
    Lure[] allLures = Lure.values();
    List<Lure> activeLureOfferings = new ArrayList<>();
    for (Lure l: allLures) {
      if (l.getRodLevelUnlocked() <= this.rodLevel && l.getRodLevelUnlocked() != 0) {
        activeLureOfferings.add(l);
      }
    }
    return activeLureOfferings;
  }
}
