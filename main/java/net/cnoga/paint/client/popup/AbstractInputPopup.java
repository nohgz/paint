package net.cnoga.paint.client.popup;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;

/**
 * Abstract base class for input dialogs in the paint program.
 * <p>
 * Provides a consistent framework for creating dialogs with custom content and standardized button
 * handling. Uses a lazy initialization strategy to ensure subclass fields are fully initialized
 * before the UI is built.
 * </p>
 *
 * <h2>Behavior</h2>
 * <ul>
 *   <li>All popups have a minimum size to maintain visual consistency.</li>
 *   <li>Only one popup can be active at a time. If a new popup is shown,
 *       the previous one will be closed.</li>
 *   <li>The button bar is an {@link HBox} with equally spaced buttons,
 *       ensuring consistent layout across dialogs.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * Subclasses should implement:
 * <ul>
 *   <li>{@link #buildContent()} to provide the main UI content.</li>
 *   <li>{@link #onConfirm()} to handle the confirmation action.</li>
 *   <li>Optionally override {@link #buildButtonBar()} to customize buttons.</li>
 * </ul>
 *
 * <p>
 * The dialog UI is constructed lazily on the first call to {@link #show()},
 * preventing issues with uninitialized subclass fields during construction.
 * </p>
 */
@EventBusSubscriber
public abstract class AbstractInputPopup extends EventBusPublisher {

  /**
   * Default minimum width for popups.
   */
  private static final double MIN_WIDTH = 300;
  /**
   * Default minimum height for popups.
   */
  private static final double MIN_HEIGHT = 150;
  /**
   * Currently active popup, ensures only one is visible at a time.
   */
  private static AbstractInputPopup activePopup;
  /**
   * The stage representing this dialog window.
   */
  protected final Stage dialogStage;
  /**
   * Root container for the dialog content and controls.
   */
  protected final BorderPane rootPane;
  /**
   * Ensures the UI is initialized only once.
   */
  private boolean initialized = false;

  /**
   * Creates a new input popup dialog with the given title.
   *
   * @param title the window title of the popup
   */
  public AbstractInputPopup(String title) {
    dialogStage = new Stage();
    dialogStage.setTitle(title);
    dialogStage.initModality(Modality.APPLICATION_MODAL);

    // enforce consistent size
    dialogStage.setMinWidth(MIN_WIDTH);
    dialogStage.setMinHeight(MIN_HEIGHT);
    dialogStage.setResizable(false);

    rootPane = new BorderPane();
    Scene scene = new Scene(rootPane);
    dialogStage.setScene(scene);

    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ENTER -> {
          event.consume();
          onConfirm();
          dialogStage.close();
        }
        case ESCAPE -> {
          event.consume();
          onCancel();
          dialogStage.close();
        }
      }
    });

    bus.register(this);
  }

  /**
   * Initializes the dialog UI. Ensures the content and button bar are created only once, after
   * subclass fields are initialized.
   */
  private void init() {
    if (!initialized) {
      Pane content = buildContent();
      VBox wrapper = new VBox(content);
      wrapper.setAlignment(Pos.CENTER);
      wrapper.setSpacing(10);
      wrapper.setStyle("-fx-padding: 20;");

      rootPane.setCenter(wrapper);
      rootPane.setBottom(buildButtonBar());
      initialized = true;
    }
  }

  /**
   * Displays the popup dialog and waits until it is closed.
   * <p>
   * Ensures that only one popup is visible at a time by closing any previously active popup before
   * showing this one.
   * </p>
   */
  public void show() {
    if (activePopup != null && activePopup != this) {
      activePopup.close();
    }
    activePopup = this;

    init();
    dialogStage.showAndWait();

    // clear reference when closed
    activePopup = null;
  }

  /**
   * Subclasses must provide the main content of the dialog.
   *
   * @return the pane to display as the dialog content
   */
  protected abstract Pane buildContent();

  /**
   * Called when the OK/Confirm button is pressed. Subclasses should validate input and publish any
   * relevant events.
   */
  protected abstract void onConfirm();

  /**
   * Builds the default button bar with equally spaced OK and Cancel buttons.
   * <p>
   * Subclasses may override this to provide additional buttons.
   * </p>
   *
   * @return an {@link HBox} containing OK and Cancel buttons
   */
  protected HBox buildButtonBar() {
    Button okButton = new Button("OK");
    okButton.setMaxWidth(Double.MAX_VALUE);
    okButton.setOnAction(e -> {
      onConfirm();
      dialogStage.close();
    });

    Button cancelButton = new Button("Cancel");
    cancelButton.setMaxWidth(Double.MAX_VALUE);
    cancelButton.setOnAction(e -> {
      onCancel();
      dialogStage.close();
    });

    HBox box = new HBox(10, okButton, cancelButton);
    box.setStyle("-fx-alignment: center; -fx-padding: 10;");

    HBox.setHgrow(okButton, Priority.ALWAYS);
    HBox.setHgrow(cancelButton, Priority.ALWAYS);

    return box;
  }

  /**
   * Called when Cancel is pressed. By default, simply closes the dialog. Subclasses may override
   * this to publish cancellation events or perform additional cleanup.
   */
  protected void onCancel() {
    dialogStage.close();
  }

  /**
   * Closes this popup programmatically.
   */
  public void close() {
    dialogStage.close();
  }
}