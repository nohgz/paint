package net.cnoga.paint.client.popup;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.NewWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.ShowNewWorkspacePopupRequest;

/**
 * Popup dialog for creating a new workspace/canvas.
 *
 * <p>
 * Allows the user to specify width and height for the new workspace. Input is validated
 * to prevent excessively large canvases. Posts a {@link net.cnoga.paint.core.bus.events.request.NewWorkspaceRequest}
 * on confirmation.
 * </p>
 */
@EventBusSubscriber
public class NewWorkspacePopup extends AbstractInputPopup {

  /** Text field for entering canvas width. */
  private TextField widthField;

  /** Text field for entering canvas height. */
  private TextField heightField;

  /**
   * Constructs a new workspace popup with the title "New Canvas".
   */
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
      System.out.println("[NewWorkspacePopup.onConfirm] Caught invalid numerical input!");
    }
  }

  /**
   * Opens the popup when a {@link net.cnoga.paint.core.bus.events.request.ShowNewWorkspacePopupRequest}
   * is received.
   *
   * @param req the event requesting a new workspace popup
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onOpen(ShowNewWorkspacePopupRequest req) {
    super.show();
  }
}