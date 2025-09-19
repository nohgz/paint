package net.cnoga.paint.popup;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.NewWorkspaceRequest;
import net.cnoga.paint.events.request.ShowNewWorkspacePopupRequest;

@EventBusSubscriber
public class NewWorkspacePopup extends AbstractInputPopup {

  private TextField widthField, heightField;

  public NewWorkspacePopup() {
    super("New Canvas");
  }

  @Override
  protected GridPane buildContent() {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    widthField = new TextField("800");
    heightField = new TextField("600");

    grid.addRow(0, new Label("Width:"), widthField);
    grid.addRow(1, new Label("Height:"), heightField);

    return grid;
  }

  @Override
  protected void onConfirm() {
    try {
      int width = Integer.parseInt(widthField.getText());
      int height = Integer.parseInt(heightField.getText());

      if (width * height
        <= 3686400) { // Magic number is 2560x1440. The largest image before a canvas blows up.
        bus.post(new NewWorkspaceRequest(width, height));
      } else {
        throw new NumberFormatException("Canvas is too large!");
      }
    } catch (NumberFormatException e) {
      // post a new popup, but thats a todo
      System.out.println("[NewWorkspacePopup.onConfirm] Caught invalid numerical input!");
    }
  }

  @SubscribeEvent
  protected void onOpen(ShowNewWorkspacePopupRequest req) {
    super.show();
  }
}