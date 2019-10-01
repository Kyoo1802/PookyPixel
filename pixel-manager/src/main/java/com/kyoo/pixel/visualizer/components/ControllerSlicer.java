package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelController;
import com.kyoo.pixel.visualizer.data.RgbLedStrips;
import java.util.Optional;

public interface ControllerSlicer {

  Optional<RgbLedStrips> slice(Optional<RgbLedStrips> frame);

  void update(PixelController controller);
}
