package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ResizeFrameDecorator implements FrameDecorator {

  private static BufferedImage resize(BufferedImage image, int width, int height) {
    BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int pixel = image.getRGB(x * imageWidth / width, y * imageHeight / height);
        newImg.setRGB(x, y, pixel);
      }
    }
    return newImg;
  }

  @Override
  public Optional<BufferedImage> decorate(Optional<BufferedImage> frame,
      PixelConnection pixelConnection) {
    if (frame.isPresent()) {
      frame = Optional.of(resize(frame.get(), pixelConnection.getWidth(),
          pixelConnection.getHeight()));
    }
    return frame;
  }
}
