package com.kyoo.pixel;

import com.google.inject.Singleton;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class MainKeyHandler implements EventHandler<KeyEvent> {

  private ConcurrentLinkedQueue<EventHandler<KeyEvent>> concurrentLinkedQueue;

  public MainKeyHandler() {
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
