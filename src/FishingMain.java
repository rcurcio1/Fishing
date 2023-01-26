import Controller.FishingController;
import Controller.FishingControllerImpl;
import Model.FishingModel;
import Model.FishingModelImpl;
import View.FishingTextView;
import View.FishingView;
import java.io.InputStreamReader;

public class FishingMain {
  public static void main(String[] args) {
    FishingModel model = new FishingModelImpl();
    FishingView view = new FishingTextView(model);
    FishingController controller = new FishingControllerImpl(model, view,
        new InputStreamReader(System.in));
    controller.playGame();
  }
}

/* IDEAS
 *     
 * Cost to travel to new location
 * 
 * Possibility to catch junk / special locational items
 * 
 * Lure ideas
 * -Rarer fish
 * -Larger fish
 *
 *
 * Create a state class so that the game can be started paused and then started from the same place
 * 
 */