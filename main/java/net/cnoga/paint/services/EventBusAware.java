package net.cnoga.paint.services;

import net.cnoga.paint.bus.EventBus;

public abstract class EventBusAware {

  protected final EventBus bus;

  public EventBusAware(EventBus bus) {
    this.bus = bus;
  }
}
