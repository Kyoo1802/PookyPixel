package com.kyoo.pixel.visualizer.components.capturer;

import com.kyoo.pixel.visualizer.data.PixelFrame;
import java.util.Optional;

public interface Capturer {

  Optional<PixelFrame> getFrame();

  void stop();
}
