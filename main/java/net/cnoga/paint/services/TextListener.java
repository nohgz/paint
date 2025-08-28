package net.cnoga.paint.services;

import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ImageSaveEvent;
import net.cnoga.paint.events.ImageOpenEvent;

@EventBusSubscriber
public class TextListener {

  @SubscribeEvent
  public void handleFileSaveEvent(ImageSaveEvent imageSaveEvent) {
    System.out.println("[TextListener] Sees file save event.");
  }

  @SubscribeEvent
  public void handleImageOpenEvent(ImageOpenEvent imageOpenEvent) {
    System.out.println("[TextListener] Sees open file event.");
  }
}
