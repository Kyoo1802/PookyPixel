package com.kyoo.pixel.visualizer.components.out;

import com.kyoo.pixel.visualizer.data.FrameData;

public interface FrameOutSubscriber {

  void receive(FrameData frameData);
}
