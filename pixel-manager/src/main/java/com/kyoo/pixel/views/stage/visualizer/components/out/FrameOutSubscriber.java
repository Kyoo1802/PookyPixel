package com.kyoo.pixel.views.stage.visualizer.components.out;

import com.kyoo.pixel.views.stage.visualizer.data.SerializedFrame;

public interface FrameOutSubscriber {

  void receive(SerializedFrame serializedFrame);
}
