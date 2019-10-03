package com.kyoo.pixel.visualizer.components.parser;

import com.kyoo.pixel.data.PixelConnection;
import com.kyoo.pixel.data.PixelConnectionChannel;
import com.kyoo.pixel.visualizer.data.ImageFrame;
import com.kyoo.pixel.visualizer.data.ControllerLedStrips;
import java.awt.Point;

public final class ConnectionParser {

  private PixelConnection pixelConnection;

  public ControllerLedStrips parse(ImageFrame imageFrame) {
    ControllerLedStrips controllerLedStrips = new ControllerLedStrips();
    for (PixelConnectionChannel channel : pixelConnection.getChannels()) {
      for (Point ledPosition : channel.getLedPositions()) {
        Point framePosition = pixelConnection.toFramePosition(ledPosition);
        int ledRgb = imageFrame.getBufferedImage().getRGB(framePosition.x, framePosition.y);
        controllerLedStrips.addLedRgb(channel.getId(), ledRgb);
      }
    }
    return controllerLedStrips;
  }

  public void update(PixelConnection connection) {
    this.pixelConnection = pixelConnection;
  }
}
