package com.kyoo.pixel.views.stage.visualizer.components.decorator;

import com.kyoo.pixel.model.PixelConnection;

import java.awt.image.BufferedImage;

public interface ImageDecorator {

  BufferedImage decorate(BufferedImage image, PixelConnection pixelConnection);
}
