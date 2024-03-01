package Model;

import SpeciesInfo.FishLocations;
import SpeciesInfo.Species;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

public class FishingModel {
  private final int WAIT_TIME = 5000;
  private FishLocations fishLocations;
  private FishingState state;

  public FishingModel() {
    this.fishLocations = new FishLocations();
    this.state = new FishingState(this.fishLocations);
  }

  public FishingModel(String filename) throws FileNotFoundException {
    this.fishLocations = new FishLocations();
    this.state = new FishingState(this.fishLocations, filename);
  }
  
  public void addFishToInventory(Fish f) {
    this.state.addToInventory(f);
  }

  
  public void changeLocation(String location, Water w) {
    this.state.setLocation(location);
    this.state.setWaterType(w);
  }

  
  public void upgradeRod() {
    this.state.setRodLevel(this.state.getRodLevel() + 1);
  }

  
  public void changeMoney(int d) {
    this.state.setMoney(this.state.getMoney() + d);
  }

  
  public int getMoney() {
    return this.state.getMoney();
  }

  
  public List<Fish> getInventory() {
    return this.state.getInventory();
  }

  
  public void removeFishFromInventory(int i) {
    this.state.removeFromInventory(i);
  }

  
  public int getRodLevel() {
    return this.state.getRodLevel();
  }

  
  public String getLocation() {
    return this.state.getLocation();
  }

  
  public Water getWaterType() {
    return this.state.getWaterType();
  }

  
  public Boolean isGameOver() {
    return false;
  }

  public FishLocations getFishLocation() {
    return this.fishLocations;
  }

  
  public Species getRandomFishByLocation() {
    List<Species> options = new ArrayList<Species>();
    for (Species s: this.fishLocations.getFishLocations().get(this.getLocation())) {
      if (s.getWaterType() == this.getWaterType()) {
        options.add(s);
      }
    }
    return this.getRandomFish(options);
  }

  private Species getRandomFish(List<Species> options) {
    Random rand = new Random();
    int i = rand.nextInt(options.size());
    Species s = options.get(i);
    if (rand.nextInt(Math.max(s.getRarity().value / this.getRarityDivider(), 1)) == 0) {
      return s;
    }
    else {
      return getRandomFish(options);
    }
  }

  private int getRarityDivider() {
    switch (this.getLure()) {
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

  
  public int getInventoryCost() {
    int cost = 0;
    for (Fish f: this.getInventory()) {
      cost += f.getWorth();
    }
    return cost;
  }

  
  public void emptyInventory() {
    this.state.setInventory(new ArrayList<Fish>());    
  }

  
  public Map<String, List<Species>> getFishLocations() {
    return this.fishLocations.getFishLocations();
  }

  
  public void setLocation(String l) {
    this.state.setLocation(l);
  }

  
  public List<Water> getWaterLocations(String location) {
    return this.fishLocations.getWaterForLocation(location);
  }

  public List<String> getLocations() {
    return this.fishLocations.getLocations();
  }
  
  public void setWater(Water w) {
    this.state.setWaterType(w);  
  }

  
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
    this.updateAlmanac(f.getName(), f.getWeight());
    return f;
  }

  private double calculatwWeight(Species s) {
    double difference = s.getMaxWeight() - s.getMinWeight();
    double base = s.getMinWeight() + (difference * this.getRodLevel() / 100);
    switch(this.getLure()) {
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
    if (this.getLureDuration() > 0) {
      this.decreaseLureDuration();
    }
    if (this.getLureDuration() == 0) {
      this.setLure(Lure.NO_LURE, 0);
    }
  }

  private void decreaseLureDuration() {
    this.state.setLureDuration(this.state.getLureDuration() - 1);
  }

  private void updateAlmanac(String name, double weight) {
    this.state.updateAlmanac(name, weight);
  }

  private long getWaitTime() {
    int base = WAIT_TIME - (this.getRodLevel() * (WAIT_TIME / 100));
    //Random rand = new Random(); TODO: re-add randomization of wait times
    //base -= rand.nextInt(100);
    switch (this.getLure()) {
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

  
  public Lure getLure() {
    return this.state.getLure();
  }

  
  public int getLureDuration() {
    return this.state.getLureDuration();
  }

  
  public Map<String, Map<Water, Map<String, Double>>> getAlmanac() {
    return this.state.getAlmanac();
  }

  
  public void setLure(Lure lure, int duration) {
    if (this.getLure().equals(lure)) {
      this.state.setLureDuration(this.getLureDuration() + duration);
    }
    else {
      this.state.setLureDuration(duration);
    }
    this.state.setLure(lure);
  }

  
  public List<Lure> getActiveLureOfferings() {
    Lure[] allLures = Lure.values();
    List<Lure> activeLureOfferings = new ArrayList<>();
    for (Lure l: allLures) {
      if (l.getRodLevelUnlocked() <= this.getRodLevel() && l.getRodLevelUnlocked() != 0) {
        activeLureOfferings.add(l);
      }
    }
    return activeLureOfferings;
  }

  
  public int getNumCatchableFish() {
    return this.fishLocations.getNumSpecies();
  }

  public void saveState(String filename) throws JsonIOException, IOException {
    JsonObject json = this.state.makeJsonFromState();
    Gson gson = new Gson();
    Writer writer = new FileWriter(filename + ".json");
    gson.toJson(json, writer);
    writer.close();
  }
}
