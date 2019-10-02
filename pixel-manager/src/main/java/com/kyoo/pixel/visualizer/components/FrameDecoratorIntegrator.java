package com.kyoo.pixel.visualizer.components;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.PixelConnection;
import java.awt.image.BufferedImage;
import java.util.Set;

public final class FrameDecoratorIntegrator {

  private final Set<FrameDecorator> editDecorators;
  private PixelConnection pixelConnection;

  @Inject
  public FrameDecoratorIntegrator(Set<FrameDecorator> editDecorators) {
    this.editDecorators = editDecorators;
  }

  public BufferedImage decorate(BufferedImage image) {
    Preconditions.checkNotNull(pixelConnection);
    for (FrameDecorator decorator : editDecorators) {
      image = decorator.decorate(image, pixelConnection);
    }
    return image;
  }

  public void update(PixelConnection pixelConnection) {
    this.pixelConnection = pixelConnection;
  }
}
