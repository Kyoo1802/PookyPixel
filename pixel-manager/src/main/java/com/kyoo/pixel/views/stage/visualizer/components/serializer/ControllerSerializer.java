package com.kyoo.pixel.views.stage.visualizer.components.serializer;

import com.kyoo.pixel.model.PixelController;
import com.kyoo.pixel.views.stage.visualizer.data.ControllerLedStrips;
import com.kyoo.pixel.views.stage.visualizer.data.ControllerLedStrips.RgbLed;
import com.kyoo.pixel.views.stage.visualizer.data.SerializedFrame;

import java.util.LinkedList;
import java.util.Map.Entry;

public final class ControllerSerializer {

  private PixelController pixelController;

  public SerializedFrame serialize(ControllerLedStrips ledStrips) {
    SerializedFrame serializedFrame = new SerializedFrame();
    for (Entry<Integer, LinkedList<RgbLed>> entryStrip :
        ledStrips.getStripsByChannelId().entrySet()) {
      int countLed = 0;
      for (RgbLed led : entryStrip.getValue()) {
        serializedFrame.add(
            entryStrip.getKey(), countLed / pixelController.getSerialSlicedSize(), led);
        countLed++;
      }
    }
    return serializedFrame;
  }

  public void update(PixelController pixelController) {
    this.pixelController = pixelController;
  }
}
