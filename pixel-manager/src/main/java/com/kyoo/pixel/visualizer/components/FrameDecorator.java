package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import java.awt.image.BufferedImage;
import java.util.Optional;

public interface FrameDecorator {

  Optional<BufferedImage> decorate(Optional<BufferedImage> frame, PixelConnection pixelConnection);
}
