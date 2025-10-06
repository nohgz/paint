package net.cnoga.paint.core.brews;

import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;

@EventBusSubscriber
public class LoggerBrew extends EventBusPublisher {
  // spawn a thread that listens for events using log4j
  // effectively, just a whole bunch of subscribe events lmao.
  // This is another reason the event bus is on top
}
