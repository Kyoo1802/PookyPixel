package com.kyoo.pixel.visualizer.components.out;

import com.kyoo.pixel.visualizer.data.FrameData;
import com.kyoo.pixel.visualizer.data.FrameData.SerializedMetadata;
import com.kyoo.pixel.visualizer.data.RgbLedStrips.RgbLed;
import java.util.LinkedList;
import java.util.Map.Entry;

public final class ConsoleSubscriber implements FrameOutSubscriber {

  @Override
  public void receive(FrameData frameData) {
    System.out.println("\nFrame Data: "+frameData.getFrameNumber());
    for(Entry<SerializedMetadata, LinkedList<RgbLed>> entry: frameData.getSerializedData().entrySet()){
      System.out.printf("%d %d: %s\n", entry.getKey().getChannelId(), entry.getKey().getStartIdx(),
          entry.getValue());
    }
  }
}
