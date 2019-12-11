package com.kyoo.pixel.views.stage.visualizer.data;

import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public final class ImageFrame {

  private BufferedImage bufferedImage;
  private int frameNumber;
  private int frameRate;
}
