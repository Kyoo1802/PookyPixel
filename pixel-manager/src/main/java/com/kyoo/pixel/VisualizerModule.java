package com.kyoo.pixel;

import com.google.inject.AbstractModule;
import com.kyoo.pixel.visualizer.components.Capturer;
import com.kyoo.pixel.visualizer.components.ScreenCapturer;

final class VisualizerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Capturer.class).to(ScreenCapturer.class);
  }
}
