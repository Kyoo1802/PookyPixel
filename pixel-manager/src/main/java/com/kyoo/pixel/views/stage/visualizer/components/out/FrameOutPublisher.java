package com.kyoo.pixel.views.stage.visualizer.components.out;

import com.google.inject.Inject;
import com.kyoo.pixel.views.stage.visualizer.data.SerializedFrame;

import java.util.Set;

public class FrameOutPublisher {

  private final Set<FrameOutSubscriber> subscribers;

  @Inject
  public FrameOutPublisher(Set<FrameOutSubscriber> subscribers) {
    this.subscribers = subscribers;
  }

  public void publish(SerializedFrame serializedFrame) {
    for (FrameOutSubscriber subscriber : subscribers) {
      subscriber.receive(serializedFrame);
    }
  }
}
