package View;

import java.io.IOException;

public interface FishingView {

  void renderMessage(String message);

  void renderFish();

  void renderLocations() throws IOException;

  void renderWaterTypes(String location);

  void renderInventory() throws IOException;

  void renderBuyEquipmentMenu();

  void renderSellFishMenu();

  void renderGoFishMenu();

  void renderChangeLocationMenu();

  void renderChangeWaterMenu(String location);

  void renderSaveStateMenu();

  void renderMainMenu();

  void renderAlmanac();
}
