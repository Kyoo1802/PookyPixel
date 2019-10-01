package com.kyoo.pixel.visualizer.components;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface Capturer {

  Optional<BufferedImage> getFrame();

  void stop();
}
