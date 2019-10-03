package com.kyoo.pixel.visualizer.components.parser;

import com.kyoo.pixel.data.PixelConnection;
import com.kyoo.pixel.data.PixelConnectionChannel;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import com.kyoo.pixel.visualizer.data.RgbLedStrips;
import java.awt.Point;

public final class ConnectionParser {

  private PixelConnection pixelConnection;

  public RgbLedStrips parse(PixelFrame pixelFrame) {
    RgbLedStrips rgbLedStrips = new RgbLedStrips();
    for (PixelConnectionChannel channel : pixelConnection.getChannels()) {
      for (Point ledPosition : channel.getLedPositions()) {
        Point framePosition = pixelConnection.toFramePosition(ledPosition);
        int ledRgb = pixelFrame.getBufferedImage().getRGB(framePosition.x, framePosition.y);
        rgbLedStrips.addLedRgb(channel.getId(), ledRgb);
      }
    }
    return rgbLedStrips;
  }

  public void update(PixelConnection connection) {
    this.pixelConnection = pixelConnection;
  }
}
