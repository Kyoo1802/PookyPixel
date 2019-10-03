package com.kyoo.pixel.visualizer;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.kyoo.pixel.visualizer.components.capturer.Capturer;
import com.kyoo.pixel.visualizer.components.capturer.FrameStats;
import com.kyoo.pixel.visualizer.components.capturer.ScreenCapturer;
import com.kyoo.pixel.visualizer.components.decorator.FrameDecorator;
import com.kyoo.pixel.visualizer.components.decorator.FrameDecoratorIntegrator;
import com.kyoo.pixel.visualizer.components.decorator.ResizeFrameDecorator;
import com.kyoo.pixel.visualizer.components.out.CanvasSubscriber;
import com.kyoo.pixel.visualizer.components.out.ConsoleSubscriber;
import com.kyoo.pixel.visualizer.components.out.FrameOutPublisher;
import com.kyoo.pixel.visualizer.components.out.FrameOutSubscriber;
import com.kyoo.pixel.visualizer.components.out.WifiSubscriber;
import com.kyoo.pixel.visualizer.components.parser.ConnectionParser;
import com.kyoo.pixel.visualizer.components.serializer.ControllerSerializer;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class VisualizerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(FrameStats.class);
    bind(Capturer.class).to(ScreenCapturer.class);
    Names.bindProperties(binder(), loadProperties());

    bind(FrameDecoratorIntegrator.class);
    Multibinder<FrameDecorator> frameDecoratorBinder = Multibinder
        .newSetBinder(binder(), FrameDecorator.class);
    frameDecoratorBinder.addBinding().to(ResizeFrameDecorator.class);
    bind(ConnectionParser.class);
    bind(ControllerSerializer.class);

    bind(FrameOutPublisher.class);
    Multibinder<FrameOutSubscriber> publisherBinder = Multibinder
        .newSetBinder(binder(), FrameOutSubscriber.class);
    publisherBinder.addBinding().to(CanvasSubscriber.class);
    publisherBinder.addBinding().to(ConsoleSubscriber.class);
    publisherBinder.addBinding().to(WifiSubscriber.class);
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
