package com.kyoo.pixel.visualizer.components;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.PixelController;
import com.kyoo.pixel.visualizer.data.PixelFrame;

public interface ControllerSlicer {

  PixelFrame slice(PixelFrame frame);

  void update(PixelController controller);
}
