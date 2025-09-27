package net.cnoga.paint.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import net.cnoga.paint.core.bus.EventBus;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;

public class EventBusTest {

  static record TestEvent(String message) {

  }

  @EventBusSubscriber
  static class TestSubscriber {
    String lastMessage;

    @SubscribeEvent
    public void onTestEvent(TestEvent evt) {
      lastMessage = evt.message;
    }
  }

  @EventBusSubscriber
  static class TestMultiSubscriber {
    int count = 0;
    int count2 = 0;

    @SubscribeEvent
    private void handle(TestEvent evt) {
      count++;
    }

    @SubscribeEvent
    private void handle2(TestEvent evt) {
      count2 += 2;
    }
  }

  @EventBusSubscriber
  static class InvalidSubscriber {
    @SubscribeEvent
    public void shouldFail() {
    }
  }

  @EventBusSubscriber
  class InvalidMultiSubscriber {
    @SubscribeEvent
    public void badHandler(TestEvent e, String extra) {}
  }

  @Test
  void testSingleSubscriberReceivesEvent() {
    TestSubscriber subscriber = new TestSubscriber();
    EventBus bus = EventBus.getInstance();
    bus.register(subscriber);

    bus.post(new TestEvent("Hello"));

    assertEquals("Hello", subscriber.lastMessage);
  }

  @Test
  void testMultipleSubscribersReceiveEvent() {
    TestMultiSubscriber multiSubscriber = new TestMultiSubscriber();
    EventBus bus = EventBus.getInstance();

    bus.register(multiSubscriber);

    bus.post(new TestEvent("ooh ooh ooh"));

    assertEquals(1, multiSubscriber.count);
    assertEquals(2, multiSubscriber.count2);
  }

  @Test
  void testInvalidSubscriberThrowsException() {
    InvalidSubscriber invalidSubscriber = new InvalidSubscriber();
    EventBus bus = EventBus.getInstance();

    assertThrows(IllegalArgumentException.class, () -> bus.register(invalidSubscriber));
  }

  @Test
  void testSubscriberWithWrongParameterCountThrowsException() {
    EventBus bus = EventBus.getInstance();
    InvalidMultiSubscriber bad = new InvalidMultiSubscriber();

    assertThrows(IllegalArgumentException.class, () -> bus.register(bad));
  }
}
