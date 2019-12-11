package com.kyoo.pixel.views.stage.visualizer.components.out;

import com.kyoo.pixel.views.stage.visualizer.data.ControllerLedStrips.RgbLed;
import com.kyoo.pixel.views.stage.visualizer.data.SerializedFrame;
import com.kyoo.pixel.views.stage.visualizer.data.SerializedFrame.SerializedMetadata;

import java.util.LinkedList;
import java.util.Map.Entry;

public final class ConsoleSubscriber implements FrameOutSubscriber {

  @Override
  public void receive(SerializedFrame serializedFrame) {
    for (Entry<SerializedMetadata, LinkedList<RgbLed>> entry :
        serializedFrame.getSerializedData().entrySet()) {}
  }
}
