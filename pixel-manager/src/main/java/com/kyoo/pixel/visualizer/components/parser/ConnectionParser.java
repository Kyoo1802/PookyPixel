package com.kyoo.pixel.visualizer.components.parser;

import com.kyoo.pixel.data.PixelConnection;
import com.kyoo.pixel.data.PixelConnectionChannel;
import com.kyoo.pixel.visualizer.data.ControllerLedStrips;
import com.kyoo.pixel.visualizer.data.ImageFrame;
import java.awt.Point;

public final class ConnectionParser {

  private PixelConnection pixelConnection;

  public ControllerLedStrips parse(ImageFrame imageFrame) {
    ControllerLedStrips controllerLedStrips = new ControllerLedStrips();
    Point startPosition = pixelConnection.getStartPosition();
    for (PixelConnectionChannel channel : pixelConnection.getChannels()) {
      for (Point connectionPosition : channel.getConnectionPositions()) {
        Point framePosition = toAbsolutePosition(startPosition, connectionPosition);
        int ledRgb = imageFrame.getBufferedImage().getRGB(framePosition.x, framePosition.y);
        controllerLedStrips.addLedRgb(channel.getId(), ledRgb);
      }
    }
    return controllerLedStrips;
  }

  private Point toAbsolutePosition(Point startPosition, Point relativePosition) {
    return new Point(relativePosition.x - startPosition.x, relativePosition.y - startPosition.y);
  }

  public void update(PixelConnection pixelConnection) {
    this.pixelConnection = pixelConnection;
  }
}
