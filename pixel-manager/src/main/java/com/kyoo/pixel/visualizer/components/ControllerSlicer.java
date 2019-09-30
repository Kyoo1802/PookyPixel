package com.kyoo.pixel.visualizer.components;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.PixelController;
import com.kyoo.pixel.visualizer.data.PixelFrame;

public final class ControllerSlicer {
  private PixelController controller;

  @Inject
  public ControllerSlicer(PixelController controller){
    this.controller = controller;
  }

  public PixelFrame slice(PixelFrame frame) {
    return null;
  }

  public void update(PixelController controller) {
    this.controller = controller;
  }
}
