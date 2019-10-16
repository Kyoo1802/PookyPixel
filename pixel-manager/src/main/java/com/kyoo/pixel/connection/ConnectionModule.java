package com.kyoo.pixel.connection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
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
    cp.driverPortColor(properties.getProperty("connection.renderer.port.color"));
    cp.ledStartColor(properties.getProperty("connection.renderer.led.start.color"));
    cp.ledOffColor(properties.getProperty("connection.renderer.led.off.color"));
    cp.ledEndColor(properties.getProperty("connection.renderer.led.end.color"));
    cp.selectColor(properties.getProperty("connection.renderer.select.color"));
    cp.selectWidth(Integer.parseInt(properties.getProperty("connection.renderer.select.width")));
    cp.noActionColor(properties.getProperty("connection.renderer.noaction.color"));
    cp.ledConnectionPathWidth(
        Integer.parseInt(properties.getProperty("connection.renderer.connectionpath.width")));
    cp.ledConnectionPathColor(properties.getProperty("connection.renderer.connectionpath.color"));
    cp.mouseColor(properties.getProperty("connection.renderer.mouse.color"));
    cp.mouseWidth(Integer.parseInt(properties.getProperty("connection.renderer.mouse.width")));

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
