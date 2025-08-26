package net.cnoga.paint.services;

import net.cnoga.paint.bus.EventBus;

public abstract class EventBusProvider {
  protected final EventBus bus;

  public EventBusProvider(EventBus bus) {
    this.bus = bus;
  }
}
