package com.kyoo.pixel.views.stage.visualizer.components.capturer;

import com.kyoo.pixel.views.stage.visualizer.data.ImageFrame;

import java.util.Optional;

public interface Capturer {

  Optional<ImageFrame> getImageFrame();

  void stop();
}
