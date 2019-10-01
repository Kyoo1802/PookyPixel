package com.kyoo.pixel;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.kyoo.pixel.visualizer.components.Capturer;
import com.kyoo.pixel.visualizer.components.ScreenCapturer;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

@Log4j2
final class VisualizerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Capturer.class).to(ScreenCapturer.class);
    Names.bindProperties(binder(), loadProperties());
  }

  private Properties loadProperties() {
    Properties properties = new Properties();
    try {
      properties
          .load(ClassLoader.getSystemClassLoader().getResourceAsStream("visualizer.properties"));
    } catch (IOException e) {
      log.error("Not possible to load properties", e);
    }
    return properties;
  }
}
