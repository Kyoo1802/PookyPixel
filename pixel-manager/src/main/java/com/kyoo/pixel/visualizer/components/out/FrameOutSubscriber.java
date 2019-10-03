package com.kyoo.pixel.visualizer.components.out;

import com.kyoo.pixel.visualizer.data.SerializedFrame;

public interface FrameOutSubscriber {

  void receive(SerializedFrame serializedFrame);
}
