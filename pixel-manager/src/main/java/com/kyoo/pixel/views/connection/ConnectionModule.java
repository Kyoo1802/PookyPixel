package com.kyoo.pixel.views.connection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.kyoo.pixel.data.connection.ConnectionProperties;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

/**
 *
 */
@Log4j2
public class ConnectionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ConnectionView.class);
    bind(ConnectionViewModel.class);
    bind(ConnectionModel.class);
  }

  @Provides
  public ConnectionProperties connectionProperties() {
    Properties properties = loadProperties();

    ConnectionProperties.ConnectionPropertiesBuilder cp = ConnectionProperties.builder();
    cp.backgroundColor(properties.getProperty("connection.renderer.background.color"));
    cp.backgroundDotsColor(properties.getProperty("connection.renderer.background.dots.color"));
    cp.backgroundDotsSize(properties.getProperty("connection.renderer.background.dots.size"));
    cp.squareSize(properties.getProperty("connection.renderer.square.size"));
    cp.ledSize(properties.getProperty("connection.renderer.led.size"));
    cp.ledStartColor(properties.getProperty("connection.renderer.led.start.color"));
    cp.ledOffColor(properties.getProperty("connection.renderer.led.off.color"));
    cp.ledEndColor(properties.getProperty("connection.renderer.led.end.color"));
    cp.selectColor(properties.getProperty("connection.renderer.select.color"));
    cp.noActionColor(properties.getProperty("connection.renderer.noaction.color"));

    return cp.build();
  }

  private Properties loadProperties() {
    Properties properties = new Properties();
    try {
      properties
          .load(ClassLoader.getSystemClassLoader().getResourceAsStream("connection.properties"));
    } catch (IOException e) {
      log.error("Not possible to load properties", e);
    }
    return properties;
  }
}
