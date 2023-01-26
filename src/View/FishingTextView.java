package View;

import Model.Fish;
import Model.FishingModel;
import Model.Water;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;

public class FishingTextView implements FishingView {

  private FishingModel model;
  private Appendable ap;

  public FishingTextView(FishingModel model) {
    this.model = model;
    this.ap = System.out;
  }

  @Override
  public void renderMessage(String message) {
    try {
      this.ap.append(String.format("%s\n", message));
    }
    catch (IOException e) {
      System.out.println("Unable to render message: " + e.getMessage());
    }
  }

  @Override
  public void renderFish() {
    this.model.getInventory();
  }

  @Override
  public void renderLocations() throws IOException {
    Iterator<String> keys = this.model.getFishLocations().keySet().iterator();
    int i = 1;
    while (keys.hasNext()) {
      this.ap.append("" + i + ". " + keys.next() + "\n");
      i++;
    }    
  }

  @Override
  public void renderWaterTypes(String location) throws IOException {
    int i = 1;
    for (Water w : this.model.getWaterLocations(location)) {
      this.ap.append("" + i + ". " + w + "\n");
      i++;
    }    
  }

  @Override
  public void renderInventory() throws IOException {
    int i = 1;
    for (Fish f : this.model.getInventory()) {
      this.ap.append("" + i + ". " + f.getName() + ":\n" + "    Weight = " + f.getWeight() + "lbs\n" + 
        "    Worth = " + f.getWorth() + "\n\n");
      i++;
    }
  }

  @Override
  public void renderBuyEquipmentMenu() {
    this.renderMessage(
        "~ ~ ~ ~ ~ EQUIPMENT SHOP ~ ~ ~ ~ ~ \n" +
            "Bank: " + this.model.getMoney() + "\n" +
            "Current Rod Level: " + this.model.getRodLevel() + "\n" +
            "Current Lure: " + this.model.getLure() + " (duration " + this.model.getLureDuration() + ")\n" +
            "0. Exit\n" +
            "1. Upgrade Rod Cost: " + ((int) Math.round(Math.pow(this.model.getRodLevel(), 2) * 100)) + "\n" +
            "2. Buy Faster Bites Lure (duration 10): 75\n" +
            "3. Buy Rarer Fish Lure (duration 10): 125\n" +
            "What would you like to buy? ");
  }

  @Override
  public void renderSellFishMenu() {
    this.renderMessage(
        "~ ~ ~ ~ ~ FISH MARKET ~ ~ ~ ~ ~\n" +
            "0. Exit\n" +
            "1. Sell all fish: " + this.model.getInventoryCost() + "\n" +
            "2. Sell individual fish\n" +
            "What would you like to do? ");
  }

  @Override
  public void renderGoFishMenu() {
    this.renderMessage(
        "~ ~ ~ ~ ~ " + this.model.getLocation().toUpperCase() + " " +
            this.model.getWaterType().toString().toUpperCase() + "~ ~ ~ ~ ~\n" +
            "0. Exit\n" +
            "1. Cast Rod\n" +
            "What would you like to do?");
  }

  @Override
  public void renderChangeLocationMenu() {
    this.renderMessage("~ ~ ~ ~ ~ LOCATIONS ~ ~ ~ ~ ~");
    try {
      this.renderLocations();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.renderMessage("Enter the name of the location you would like to travel to: ");
  }

  @Override
  public void renderMainMenu() {
    this.renderMessage(
        "1. Choose new location\n" +
            "2. Sell fish\n" +
            "3. Buy new equipment\n" +
            "4. Fish here at a " + this.model.getWaterType().toString().toLowerCase() + " in " + this.model.getLocation() + "\n" +
            "5. View your almanac\n" +
            "Enter your choice: ");
  }

  public void renderAlmanac() {
    Map<String, Map<Water, Map<String, Boolean>>> almanac = this.model.getAlmanac();
    for (String loc : almanac.keySet()) {
      this.renderMessage(loc);
      for (Water water: almanac.get(loc).keySet()) {
        this.renderMessage("    " + water.toString());
        for (String spec: almanac.get(loc).get(water).keySet()) {
          if (almanac.get(loc).get(water).get(spec)) {
            this.renderMessage("        " + spec);
          }
          else {
            this.renderMessage("        ???");
          }
        }
      }
    }
  }
}
