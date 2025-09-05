package net.cnoga.paint.service;


import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.SaveStateRequest;
import net.cnoga.paint.events.response.GetSaveStateEvent;

@EventBusSubscriber
public class ProgramService extends EventBusPublisher {

  private final Stage primaryStage;

  public ProgramService(Stage primaryStage) {
    this.primaryStage = primaryStage;
    bus.register(this);
  }

  @SubscribeEvent
  private void onCloseProgram(CloseProgramRequest req) {
    System.out.println("[ProgramService] Sees close event!");
    bus.post(new SaveStateRequest());
  }

  @SubscribeEvent
  private void onGetSaveState(GetSaveStateEvent event) {
    // once we've checked the flag is not dirty we can close
    if (!event.isDirty()) {
      primaryStage.close();
    } else {
      System.out.println("Please save before exiting");
    }
  }
}
