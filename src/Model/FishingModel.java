package Model;

import java.util.List;
import java.util.Map;

import SpeciesInfo.Species;

public interface FishingModel {
  void addFishToInventory(Fish f);

  void changeLocation(String location, Water w);

  void upgradeRod();

  void changeMoney(int d);

  int getMoney();

  List<Fish> getInventory();

  void removeFishFromInventory(int i);

  int getRodLevel();

  String getLocation();

  Water getWaterType();

  Boolean isGameOver();

  public Species getRandomFishByLocation();

  int getInventoryCost();

  void emptyInventory();

  Map<String, List<Species>> getFishLocations();

  void setLocation(String l);

  List<Water> getWaterLocations(String location);

  void setWater(String s);
  
  Fish catchFish();

  Lure getLure();

  void setLure(Lure lure, int duration);

  int getLureDuration();

  Map<String, Map<Water, Map<String, Boolean>>> getAlmanac();

  List<Lure> getActiveLureOfferings();

  int getNumCatchableFish();
}
