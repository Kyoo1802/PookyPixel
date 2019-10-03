package com.kyoo.pixel.visualizer.components.capturer;

import com.kyoo.pixel.visualizer.data.ImageFrame;
import java.util.Optional;

public interface Capturer {

  Optional<ImageFrame> getImageFrame();

  void stop();
}
