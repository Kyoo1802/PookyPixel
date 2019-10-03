package com.kyoo.pixel.visualizer.components.out;

import com.kyoo.pixel.visualizer.data.ControllerLedStrips.RgbLed;
import com.kyoo.pixel.visualizer.data.SerializedFrame;
import com.kyoo.pixel.visualizer.data.SerializedFrame.SerializedMetadata;
import java.util.LinkedList;
import java.util.Map.Entry;

public final class ConsoleSubscriber implements FrameOutSubscriber {

  @Override
  public void receive(SerializedFrame serializedFrame) {
    System.out.println("\nFrame Data: " + serializedFrame.getFrameNumber());
    for (Entry<SerializedMetadata, LinkedList<RgbLed>> entry : serializedFrame.getSerializedData()
        .entrySet()) {
      System.out.printf("%d %d: %s\n", entry.getKey().getChannelId(), entry.getKey().getStartIdx(),
          entry.getValue());
    }
  }
}
