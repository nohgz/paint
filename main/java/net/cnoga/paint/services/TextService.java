package net.cnoga.paint.services;

import net.cnoga.paint.bus.EventBus;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.ImageOpenEvent;

public class TextService extends EventBusAware {

  public TextService(EventBus bus) {
    super(bus);
    bus.subscribe(FileSaveEvent.class, this::handleFileSaveEvent);
    bus.subscribe(ImageOpenEvent.class, this::handleImageOpenEvent);
  }

  public void handleFileSaveEvent(FileSaveEvent fileSaveEvent) {
    System.out.println("I AM THE TEXT SERVICE AND I SEE A FILE HAS BEEN SAVED");
  }

  public void handleImageOpenEvent(ImageOpenEvent imageOpenEvent) {
    System.out.println("I AM THE TEXT SERVICE ADN I SEE AN IMAGE IS BEING OPENED");
  }
}
