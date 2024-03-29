package com.kyoo.pixel.views.stage.visualizer.components.decorator;

import com.kyoo.pixel.model.PixelConnection;

import java.awt.image.BufferedImage;

public final class ResizeImageDecorator implements ImageDecorator {

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
  public BufferedImage decorate(BufferedImage frameImage, PixelConnection pixelConnection) {
    return resize(frameImage, pixelConnection.getWidth(), pixelConnection.getHeight());
  }
}
