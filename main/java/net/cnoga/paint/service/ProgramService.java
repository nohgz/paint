package net.cnoga.paint.service;


import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.CloseProgramRequest;

@EventBusSubscriber
public class ProgramService extends EventBusPublisher {

  private final Stage primaryStage;

  public ProgramService(Stage primaryStage) {
    this.primaryStage = primaryStage;
    bus.register(this);
  }

  @SubscribeEvent
  private void onCloseProgram(CloseProgramRequest event) {
    System.out.println("[ProgramService] Sees close event!");
    primaryStage.close();
  }
}
