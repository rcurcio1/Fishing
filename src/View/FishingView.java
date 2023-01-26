package View;

import java.io.IOException;

public interface FishingView {

  void renderMessage(String message);

  void renderFish();

  void renderLocations() throws IOException;

  void renderWaterTypes(String location) throws IOException;

  void renderInventory() throws IOException;

  void renderBuyEquipmentMenu();

  void renderSellFishMenu();

  void renderGoFishMenu();

  void renderChangeLocationMenu();

  void renderMainMenu();

  void renderAlmanac();
}
