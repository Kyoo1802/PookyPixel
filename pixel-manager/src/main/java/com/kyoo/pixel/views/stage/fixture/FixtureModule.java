package com.kyoo.pixel.views.stage.fixture;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Properties;

/** */
@Log4j2
public class FixtureModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(FixtureView.class);
    bind(FixtureViewModel.class);
    bind(FixtureModel.class);
  }

  @Provides
  public FixtureProperties connectionProperties() {
    Properties properties = loadProperties();

    FixtureProperties.FixturePropertiesBuilder cp = FixtureProperties.builder();
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
    cp.bridgeColor(properties.getProperty("connection.renderer.bridge.color"));
    cp.mouseColor(properties.getProperty("connection.renderer.mouse.color"));
    cp.mouseWidth(Integer.parseInt(properties.getProperty("connection.renderer.mouse.width")));

    return cp.build();
  }

  private Properties loadProperties() {
    Properties properties = new Properties();
    try {
      properties.load(
          ClassLoader.getSystemClassLoader().getResourceAsStream("connection.properties"));
    } catch (IOException e) {
      log.error("Not possible to load properties", e);
    }
    return properties;
  }
}
