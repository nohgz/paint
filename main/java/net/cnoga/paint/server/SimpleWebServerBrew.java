package net.cnoga.paint.server;

import java.io.IOException;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.StartServerRequest;
import net.cnoga.paint.core.bus.events.request.StopServerRequest;

@EventBusSubscriber
public class SimpleWebServerBrew extends EventBusPublisher {


  private final SimpleWebServer server;
  public SimpleWebServerBrew(SimpleWebServer server) {
    bus.register(this);
    this.server = server;
  }

  @SubscribeEvent
  private void startServer(StartServerRequest req) {
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SubscribeEvent
  private void stopServer(StopServerRequest req) {
    server.stop(0);
  }

}
