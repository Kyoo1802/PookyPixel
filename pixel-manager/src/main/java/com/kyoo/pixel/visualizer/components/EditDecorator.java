package com.kyoo.pixel.visualizer.components;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface EditDecorator {

  Optional<BufferedImage> edit(Optional<BufferedImage> frame);
}
