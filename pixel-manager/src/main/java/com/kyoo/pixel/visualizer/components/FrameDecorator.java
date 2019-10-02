package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import java.awt.image.BufferedImage;
import java.util.Optional;

public interface FrameDecorator {

  BufferedImage decorate(BufferedImage image, PixelConnection pixelConnection);
}
