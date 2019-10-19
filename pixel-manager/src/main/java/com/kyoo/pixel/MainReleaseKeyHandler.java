package com.kyoo.pixel;

import com.google.inject.Singleton;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

@Singleton
public class MainReleaseKeyHandler implements EventHandler<KeyEvent> {

  private ConcurrentLinkedQueue<EventHandler<KeyEvent>> concurrentLinkedQueue;

  public MainReleaseKeyHandler() {
    concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
  }

  @Override
  public void handle(KeyEvent event) {
    for (EventHandler<KeyEvent> handler : concurrentLinkedQueue) {
      handler.handle(event);
    }
  }

  public void attachListener(EventHandler<KeyEvent> handler) {
    concurrentLinkedQueue.add(handler);

  }
}
