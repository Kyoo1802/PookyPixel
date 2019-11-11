package com.kyoo.pixel.fixtures;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.fixtures.interactions.InteractionRequest;
import com.kyoo.pixel.fixtures.interactions.KeyboardInteraction;
import com.kyoo.pixel.fixtures.interactions.PositionInteraction;
import com.kyoo.pixel.fixtures.interactions.StateInteraction;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class FixtureViewModel {

  private final KeyboardInteraction keyboardInteraction;
  private final PositionInteraction positionInteraction;
  private final StateInteraction stateInteraction;
  private final Thread consumeInteractionThread;

  @Getter
  private ObjectProperty<ConcurrentLinkedQueue<InteractionRequest>> inputInteractions =
      new SimpleObjectProperty<>(new ConcurrentLinkedQueue<>());
  private AtomicBoolean needsRender = new AtomicBoolean(true);

  @Inject
  public FixtureViewModel(KeyboardInteraction keyboardInteraction,
      PositionInteraction positionInteraction,
      StateInteraction stateInteraction) {
    this.keyboardInteraction = keyboardInteraction;
    this.positionInteraction = positionInteraction;
    this.stateInteraction = stateInteraction;
    this.consumeInteractionThread = new Thread(() -> {
      while (true) {
        consumeInteractions();
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void startConsumingInteraction() {
    if (!consumeInteractionThread.isAlive()) {
      consumeInteractionThread.start();
    }
  }

  public void consumeInteractions() {
    while (hasPendingInteractions()) {
      InteractionRequest interactionRequest = inputInteractions.get().peek();
      switch (interactionRequest.type()) {
        case KEYBOARD:
          keyboardInteraction
              .handleInteraction(interactionRequest.getKeyboardInteractionRequest().get());
          break;
        case POSITION:
          positionInteraction
              .handleInteraction(interactionRequest.getPositionInteractionRequest().get());
          break;
        case STATE:
          stateInteraction
              .handleInteraction(interactionRequest.getStateInteractionRequest().get());
          break;
        default:
          log.error("Invalid case for interaction: " + interactionRequest.type());
      }
      log.debug("Consuming input interaction: " + interactionRequest.type());
      inputInteractions.get().poll();
      needsRender(true);
    }
  }

  public boolean needsRender(boolean needsRender) {
    return this.needsRender.getAndSet(needsRender);
  }

  public boolean hasPendingInteractions() {
    return !inputInteractions.get().isEmpty();
  }
}
