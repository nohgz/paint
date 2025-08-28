package net.cnoga.paint.bus;

public abstract class EventBusProvider {
  protected final EventBus bus;
  public EventBusProvider(EventBus bus) {
    this.bus = bus;
  }
}
