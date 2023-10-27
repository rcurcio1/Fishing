package Controller;

import Model.Fish;
import Model.Lure;
import Model.Water;
import Model.FishingModel;
import View.FishingView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonIOException;

public class FishingControllerImpl implements FishingController {
  private FishingModel model;
  private FishingView view;
  private Readable in;

  public FishingControllerImpl(FishingModel model, FishingView view, Readable in) {
    this.model = model;
    this.view = view;
    this.in = in;
  }

  @Override
  public void playGame() {
    Scanner scan = new Scanner(this.in);
    while (!model.isGameOver()) {
      this.view.renderMainMenu();
      int input = -1;
      if (scan.hasNextInt()){
        input = scan.nextInt();
      }
      else {
        return;
      }
      switch (input) {
        case 1:
          this.changeLocation(scan);
          break;
        case 2:
          this.sellFish(scan);
          break;
        case 3:
          this.buyEquipment(scan);
          break;
        case 4:
          this.goFish(scan);
          break;
        case 5:
          this.view.renderAlmanac();
          break;
        case 6:
          this.saveState(scan);
        default:
          this.view.renderMessage("Invalid input.");
          break;
      }
    }
  }

  private void saveState(Scanner scan) {
    this.view.renderSaveStateMenu();
    String filename = scan.next();
    try {
      this.model.saveState(filename);
    } catch (JsonIOException e) {
      this.view.renderMessage("Json Error: unable to save state");
      return;
    } catch (IOException e) {
      this.view.renderMessage("IO Error: unable to save state");
      return;
    }
    this.view.renderMessage("State saved successfully!");
  }

  private void changeLocation(Scanner scan) {
    int locationIndex = -1;
    String location = "";
    Water water = null;
    List<String> locations = this.model.getLocations();
    while(locationIndex != 0) {
      this.view.renderChangeLocationMenu();
      locationIndex = scan.nextInt();
      if (locationIndex > 0 && locationIndex <= locations.size()) {
        location = locations.get(locationIndex - 1);
        water = this.changeWater(location, scan);
        if (water == null) {
          locationIndex = 0;
          continue;
        }
        else {
          this.model.setLocation(location);
          this.model.setWater(water);
          return;
        }
      }
      else if (locationIndex == 0) {
        continue;
      }
      else {
        this.view.renderMessage("Invalid input.");
      }
    }
  }

  private Water changeWater(String location, Scanner scan) {
    int waterIndex = -1;
    List<Water> waters = this.model.getWaterLocations(location);
    while (waterIndex != 0) {
      this.view.renderChangeWaterMenu(location);
      waterIndex = scan.nextInt();
      if (waterIndex > 0 && waterIndex <= waters.size()) {
        return waters.get(waterIndex - 1);
      }
    }
    return null;
  }

  private void goFish(Scanner scan) {
    int input = -1;
    while (input != 0) {
      this.view.renderGoFishMenu();
      input = scan.nextInt();
      switch(input) {
        case 0:
          break;
        case 1:
          this.view.renderMessage("Casting rod...\n");
          Fish caught = this.model.catchFish();
          this.view.renderMessage("You have a bite! You caught a " + Math.round(caught.getWeight() * 100.0)/100.0 + "lb " + caught.getName() + "\n");
          this.model.addFishToInventory(caught);
          break;
        default:
          this.view.renderMessage("Invalid input.");
          break;
      }
    }
  }

  private void sellFish(Scanner scan) {
    int input = -1;
    while (input != 0) {
      this.view.renderSellFishMenu();
      input = scan.nextInt();
      switch(input) {
        case 0:
          break;
        case 1:
          this.view.renderMessage("Sold all fish for " + this.model.getInventoryCost());
          this.model.changeMoney(this.model.getInventoryCost());
          this.model.emptyInventory();
          return;
        case 2:
          try {
            this.view.renderInventory();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
          this.view.renderMessage("Enter the number of the fish you would like to sell");
          input = scan.nextInt();
          if (input > 0 && input <= this.model.getInventory().size()) {
            this.model.changeMoney(this.model.getInventory().get(input - 1).getWorth());
            this.model.removeFishFromInventory(input - 1);
          }
          else {
            this.view.renderMessage("Invalid input.");
          }
          input = -1;
          break;
        default:
          this.view.renderMessage("Invalid input.");
          break;
      }
    }
  }

  private void buyEquipment(Scanner scan) {
    int input = -1;
    while (input != 0) {
      this.view.renderBuyEquipmentMenu();
      input = scan.nextInt();
      switch(input) {
        case 0:
          break;
        case 1:
          this.upgradeRodLevel();
          break;
        default:
          this.buyLures(input);
          break;
      }
    }
  }

  private void upgradeRodLevel() {
    if(this.model.getRodLevel() >= 100) {
      this.view.renderMessage("Max rod level reached.");
    }
    else if(this.model.getMoney() >= Math.round(Math.pow(this.model.getRodLevel(), 2) * 100)) {
      this.model.changeMoney((int) Math.round(Math.pow(this.model.getRodLevel(), 2) * -100));
      this.model.upgradeRod();
    }
    else {
      this.view.renderMessage("Sorry, you do not have enough money to purchase that.");
    }
  }

  private void buyLures(int input) {
    List<Lure> activeLures = this.model.getActiveLureOfferings();
    if (input > activeLures.size() + 1 || input < 1) {
      this.view.renderMessage("Invalid input.");
      return;
    }
    Lure requestedLure = activeLures.get(input - 2);
    if (this.model.getMoney() >= requestedLure.getPrice()) {
      this.model.setLure(requestedLure, 10);
      this.model.changeMoney(-1 * requestedLure.getPrice());
    }
    else {
      this.view.renderMessage("Sorry, you do not have enough money to purchase that.");
    }
  }
}
