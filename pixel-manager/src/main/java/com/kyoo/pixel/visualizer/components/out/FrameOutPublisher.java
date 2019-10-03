package com.kyoo.pixel.visualizer.components.out;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.data.SerializedFrame;
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
