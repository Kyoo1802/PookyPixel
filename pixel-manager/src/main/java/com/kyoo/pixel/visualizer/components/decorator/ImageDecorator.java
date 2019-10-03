package com.kyoo.pixel.visualizer.components.decorator;

import com.kyoo.pixel.data.PixelConnection;
import java.awt.image.BufferedImage;

public interface FrameDecorator {

  BufferedImage decorate(BufferedImage image, PixelConnection pixelConnection);
}
