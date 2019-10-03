package com.kyoo.pixel.visualizer.components.serializer;

import com.kyoo.pixel.data.PixelController;
import com.kyoo.pixel.visualizer.data.FrameData;
import com.kyoo.pixel.visualizer.data.RgbLedStrips;
import com.kyoo.pixel.visualizer.data.RgbLedStrips.RgbLed;
import java.util.LinkedList;
import java.util.Map.Entry;

public final class ControllerSerializer {

  private PixelController pixelController;

  public FrameData serialize(RgbLedStrips ledStrips) {
    FrameData frameData = new FrameData();
    for (Entry<Integer, LinkedList<RgbLed>> entryStrip:
        ledStrips.getStripsByChannelId().entrySet()) {
      int countLed = 0;
      for(RgbLed led : entryStrip.getValue()){
        frameData.add(entryStrip.getKey(), countLed/pixelController.getSerialSlicedSize(), led);
        countLed++;
      }
    }
    return frameData;
  }

  public void update(PixelController pixelController) {
    this.pixelController = pixelController;
  }
}
