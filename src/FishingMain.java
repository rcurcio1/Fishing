import Controller.FishingController;
import Controller.FishingControllerImpl;
import Model.FishingModel;
import View.FishingTextView;
import View.FishingView;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class FishingMain {
  public static void main(String[] args) {
    FishingModel model;
    if (args.length > 0) {
      String filename = args[0];
      model = new FishingModel();
      try {
        model = new FishingModel(filename);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    else {
      model = new FishingModel();
    }
    FishingView view = new FishingTextView(model);
    FishingController controller = new FishingControllerImpl(model, view,
        new InputStreamReader(System.in));
    controller.playGame();
  }
}