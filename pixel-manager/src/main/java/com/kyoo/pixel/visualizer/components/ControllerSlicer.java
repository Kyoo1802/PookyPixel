package com.kyoo.pixel.visualizer.components;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.PixelController;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import java.util.Optional;

public interface ControllerSlicer {

  Optional<PixelFrame> slice(Optional<PixelFrame> frame);

  void update(PixelController controller);
}
