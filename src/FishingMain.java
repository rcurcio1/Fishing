import Controller.FishingController;
import Controller.FishingControllerImpl;
import Model.FishingModel;
import View.FishingTextView;
import View.FishingView;
import java.io.InputStreamReader;

public class FishingMain {
  public static void main(String[] args) {
    FishingModel model = new FishingModel();
    FishingView view = new FishingTextView(model);
    FishingController controller = new FishingControllerImpl(model, view,
        new InputStreamReader(System.in));
    controller.playGame();
  }
}