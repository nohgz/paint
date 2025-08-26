package net.cnoga.paint.services;

import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.FileSaveEvent;
import net.cnoga.paint.events.ImageOpenEvent;

@EventBusSubscriber
public class TextService{

  @SubscribeEvent
  public void handleFileSaveEvent(FileSaveEvent fileSaveEvent) {
    System.out.println("[TextService] Sees file save event.");
  }

  @SubscribeEvent
  public void handleImageOpenEvent(ImageOpenEvent imageOpenEvent) {
    System.out.println("[TextService] Sees open file event.");
  }
}
