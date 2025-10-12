package net.cnoga.paint.core.brews;

import java.io.IOException;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.StartServerRequest;
import net.cnoga.paint.core.bus.events.request.StopServerRequest;
import net.cnoga.paint.server.SimpleWebServer;

/**
 * Manages a simple web server via EventBus requests.
 * <p>
 * Listens for start and stop requests and delegates them to a {@link SimpleWebServer} instance.
 */
@EventBusSubscriber
public class SimpleWebServerBrew extends EventBusPublisher {

  private final SimpleWebServer server;

  /** Constructs the Brew with a given server instance. */
  public SimpleWebServerBrew(SimpleWebServer server) {
    bus.register(this);
    this.server = server;
  }

  /** Starts the web server when a StartServerRequest is received. */
  @SubscribeEvent
  private void startServer(StartServerRequest req) {
    try { server.start(); }
    catch (IOException e) { e.printStackTrace(); }
  }

  /** Stops the web server when a StopServerRequest is received. */
  @SubscribeEvent
  private void stopServer(StopServerRequest req) {
    server.stop(0);
  }
}
