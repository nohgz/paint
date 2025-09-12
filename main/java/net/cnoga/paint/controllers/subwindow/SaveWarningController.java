package net.cnoga.paint.controllers.subwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.init.InitSaveStageRequest;
import net.cnoga.paint.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.events.request.ForceCloseRequest;


/**
 * JavaFX controller for the "Save Before Exit" warning dialog.
 *
 * <p>This dialog appears when the user attempts to exit with unsaved changes.
 * It provides options to save the current work, cancel the exit, or exit without saving.</p>
 *
 * <p>The controller integrates with the {@link net.cnoga.paint.bus.EventBus} to coordinate save
 * and exit operations across the application.</p>
 *
 * <ul>
 *   <li>{@link #onExitAnyway(ActionEvent)} posts a {@link ForceCloseRequest} to quit immediately.</li>
 *   <li>{@link #onSave(ActionEvent)} posts a {@link WorkspaceSaveRequest} and closes the warning stage.</li>
 *   <li>{@link #onCancel(ActionEvent)} dismisses the dialog without exiting.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.0
 */
@EventBusSubscriber
public class SaveWarningController extends EventBusPublisher {

  @FXML
  public void initialize() {
    bus.register(this);
  }

  private Stage warningStage;

  @SubscribeEvent
  public void initWarningStage(InitSaveStageRequest req) {
    this.warningStage = req.warningStage();
  }

  public void onExitAnyway(ActionEvent actionEvent) {
    bus.post(new ForceCloseRequest());
  }

  // maybe this should also exit on top of saving?
  public void onSave(ActionEvent actionEvent) {
    bus.post(new WorkspaceSaveRequest());
    warningStage.close();
  }

  public void onCancel(ActionEvent actionEvent) {
    warningStage.close();
  }
}
