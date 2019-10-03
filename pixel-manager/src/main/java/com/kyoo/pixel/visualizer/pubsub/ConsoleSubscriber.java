package com.kyoo.pixel.visualizer.pubsub;

import com.kyoo.pixel.visualizer.data.FrameData;
import com.kyoo.pixel.visualizer.data.FrameData.SlicedMetadata;
import com.kyoo.pixel.visualizer.data.RgbLedStrips.RgbLed;
import java.util.LinkedList;
import java.util.Map.Entry;

public final class ConsoleSubscriber implements FrameOutSubscriber {

  @Override
  public void receive(FrameData frameData) {
    System.out.println("\nFrame Data: "+frameData.getFrameNumber());
    for(Entry<SlicedMetadata, LinkedList<RgbLed>> entry: frameData.getSlicedData().entrySet()){
      System.out.printf("%d %d: %s\n", entry.getKey().getChannelId(), entry.getKey().getStartIdx(),
          entry.getValue());
    }
  }
}
