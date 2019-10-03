package com.kyoo.pixel.visualizer.components.decorator;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kyoo.pixel.data.PixelConnection;
import java.awt.image.BufferedImage;
import java.util.Set;

public final class ImageDecoratorIntegrator {

  private final Set<ImageDecorator> imageDecorators;
  private PixelConnection pixelConnection;

  @Inject
  public ImageDecoratorIntegrator(Set<ImageDecorator> imageDecorators) {
    this.imageDecorators = imageDecorators;
  }

  public BufferedImage decorate(BufferedImage image) {
    Preconditions.checkNotNull(pixelConnection);
    for (ImageDecorator decorator : imageDecorators) {
      image = decorator.decorate(image, pixelConnection);
    }
    return image;
  }

  public void update(PixelConnection pixelConnection) {
    this.pixelConnection = pixelConnection;
  }
}
