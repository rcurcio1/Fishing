package Controller;

import Model.Fish;
import Model.FishingModel;
import Model.Lure;
import Model.Water;
import SpeciesInfo.Species;
import View.FishingView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        default:
          this.view.renderMessage("Invalid input.");
          break;
      }
    }
  }

  private void changeLocation(Scanner scan) {
    String input = "";
    String water = "";
  
    while(!input.equals("Exit")) {
      this.view.renderChangeLocationMenu();
      input = scan.nextLine();
      input.replaceAll("\\n", "");
      List<Species> newSpecies = this.model.getFishLocations().getOrDefault(input, new ArrayList<Species>());
      if (input.equals("Exit")) {
        break;
      }
      else if (newSpecies.size() == 0) {
        this.view.renderMessage("Invalid input.");
      }
      else {
        this.model.setLocation(input);
        try {
          this.view.renderWaterTypes(input);
        } catch (IOException e) {
          e.printStackTrace();
        }
        this.view.renderMessage("Enter the name of the water you would like to travel to: ");
        water = scan.next().toUpperCase();
        try {
          if (this.model.getWaterLocations(input).contains(Water.valueOf(water))) {
            this.model.setWater(water);
            return;
          }
          else {
            this.view.renderMessage("Invalid input.");
          }
        }
        catch (Exception e) {
          this.view.renderMessage("Invalid input");
        }
      }
    }
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
          this.model.changeMoney(this.model.getInventory().get(input - 1).getWorth());
          this.model.removeFishFromInventory(input - 1);
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
          break;
        case 2:
          if (this.model.getMoney() >= 75) {
            this.model.setLure(Lure.FASTER_BITES, 10);
            this.model.changeMoney(-75);
          }
          else {
            this.view.renderMessage("Sorry, you do not have enough money to purchase that.");
          }
          break;
        case 3:
          if (this.model.getMoney() >= 125) {
            this.model.setLure(Lure.RARER_FISH, 10);
            this.model.changeMoney(-125);
          }
          else {
            this.view.renderMessage("Sorry, you do not have enough money to purchase that.");
          }
        default:
          this.view.renderMessage("Invalid input.");
          break;
      }
    }
  }
}
