package net.cnoga.paint.service;


import static net.cnoga.paint.util.PaintUtil.createSubwindow;
import static net.cnoga.paint.util.PaintUtil.setSubwindowSpawnPoint;

import javafx.stage.Modality;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.ForceCloseRequest;
import net.cnoga.paint.events.request.InitSaveStageRequest;
import net.cnoga.paint.events.request.OpenGitHubRequest;
import net.cnoga.paint.events.request.SaveStateRequest;
import net.cnoga.paint.events.response.GetSaveStateEvent;
import net.cnoga.paint.util.AnchorTypes;
import net.cnoga.paint.util.PaintUtil;

@EventBusSubscriber
public class ProgramService extends EventBusPublisher {

  private final Stage primaryStage;
  private Stage warningStage;

  public ProgramService(Stage primaryStage) {
    this.primaryStage = primaryStage;
    bus.register(this);
  }

  @SubscribeEvent
  private void onCloseProgram(CloseProgramRequest req) {
    bus.post(new SaveStateRequest());
  }

  @SubscribeEvent
  private void onGetSaveState(GetSaveStateEvent event) {
    if (!event.isDirty()) {
      primaryStage.close();
    } else {
      try {
        if (warningStage == null) {
          warningStage = createSubwindow("Unsaved Changes!",
            "/net/cnoga/paint/fxml/subwindow/savewarning.fxml", primaryStage, true, 0.0, 0.0);
          setSubwindowSpawnPoint(warningStage, primaryStage, AnchorTypes.MIDDLE_CENTER);
          warningStage.initModality(Modality.APPLICATION_MODAL);
          bus.post(new InitSaveStageRequest(warningStage));
        }

        warningStage.toFront();
        warningStage.showAndWait();
      } catch (Exception e) {
        System.out.println("FUCK!");
        throw new RuntimeException(e);
      }
    }
  }

  @SubscribeEvent
  private void onForceCloseRequest(ForceCloseRequest req) {
    primaryStage.close();
  }

  @SubscribeEvent
  private void onOpenGitHub(OpenGitHubRequest req) {
    PaintUtil.openLink("https://github.com/nohgz/paint");
  }
}
