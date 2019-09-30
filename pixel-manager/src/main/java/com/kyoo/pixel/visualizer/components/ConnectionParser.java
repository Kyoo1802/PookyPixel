package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import javax.inject.Inject;

public interface ConnectionParser {

  PixelFrame parse(PixelFrame frame);

  void update(PixelConnection connection);
}
