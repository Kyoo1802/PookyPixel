package com.kyoo.pixel.visualizer.components;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.PixelConnection;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Set;

public final class FrameDecoratorIntegrator {

  private final Set<FrameDecorator> editDecorators;
  private PixelConnection pixelConnection;

  @Inject
  public FrameDecoratorIntegrator(Set<FrameDecorator> editDecorators) {
    this.editDecorators = editDecorators;
  }

  public Optional<BufferedImage> decorate(Optional<BufferedImage> frame) {
    if (pixelConnection == null) {
      return Optional.empty();
    }
    for (FrameDecorator decorator : editDecorators) {
      frame = decorator.decorate(frame, pixelConnection);
    }
    return frame;
  }

  public void update(PixelConnection pixelConnection) {
    this.pixelConnection = pixelConnection;
  }
}
