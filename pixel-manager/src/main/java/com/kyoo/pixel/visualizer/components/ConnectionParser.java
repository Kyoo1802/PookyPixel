package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import com.kyoo.pixel.visualizer.PixelConnectionChannel;
import com.kyoo.pixel.visualizer.data.RgbLedStrips;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class ConnectionParser {

  private PixelConnection pixelConnection;

  public Optional<RgbLedStrips> parse(Optional<BufferedImage> frame) {
    if (frame.isPresent()) {
      RgbLedStrips rgbLedStrips = new RgbLedStrips();
      for (PixelConnectionChannel channel : pixelConnection.getChannels()) {
        for (Point ledPosition : channel.getLedPositions()) {
          Point framePosition = pixelConnection.toFramePosition(ledPosition);
          int ledRgb = frame.get().getRGB(framePosition.x, framePosition.y);
          rgbLedStrips.addLedRgb(channel.getId(), ledRgb);
        }
      }
      Optional.of(rgbLedStrips);
    }
    return Optional.empty();
  }

  public void update(PixelConnection connection) {
    this.pixelConnection = pixelConnection;
  }
}
