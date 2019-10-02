package com.kyoo.pixel.visualizer.pubsub;

import com.kyoo.pixel.visualizer.data.FrameData;

public interface FrameOutSubscriber {

  void receive(FrameData frameData);
}
