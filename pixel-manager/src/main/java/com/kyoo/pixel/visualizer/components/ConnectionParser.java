package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import java.awt.image.BufferedImage;
import java.util.Optional;
import javax.inject.Inject;

public interface ConnectionParser {

  Optional<PixelFrame> parse(Optional<BufferedImage> frame);

  void update(PixelConnection connection);
}
