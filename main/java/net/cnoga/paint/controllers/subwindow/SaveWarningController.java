package net.cnoga.paint.controllers.subwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.FileSaveRequest;
import net.cnoga.paint.events.request.ForceCloseRequest;
import net.cnoga.paint.events.request.InitSaveStageRequest;


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
    bus.post(new FileSaveRequest());
    warningStage.close();
  }

  public void onCancel(ActionEvent actionEvent) {
    warningStage.close();
  }
}
