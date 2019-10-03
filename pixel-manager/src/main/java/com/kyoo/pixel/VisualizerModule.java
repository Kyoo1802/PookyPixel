package com.kyoo.pixel;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.kyoo.pixel.visualizer.components.Capturer;
import com.kyoo.pixel.visualizer.components.ConnectionParser;
import com.kyoo.pixel.visualizer.components.ControllerSlicer;
import com.kyoo.pixel.visualizer.components.FrameDecorator;
import com.kyoo.pixel.visualizer.components.FrameDecoratorIntegrator;
import com.kyoo.pixel.visualizer.components.ResizeFrameDecorator;
import com.kyoo.pixel.visualizer.components.ScreenCapturer;
import com.kyoo.pixel.visualizer.pubsub.CanvasSubscriber;
import com.kyoo.pixel.visualizer.pubsub.ConsoleSubscriber;
import com.kyoo.pixel.visualizer.pubsub.FrameOutPublisher;
import com.kyoo.pixel.visualizer.pubsub.FrameOutSubscriber;
import com.kyoo.pixel.visualizer.pubsub.WifiSubscriber;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

@Log4j2
final class VisualizerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Capturer.class).to(ScreenCapturer.class);
    Names.bindProperties(binder(), loadProperties());

    bind(FrameDecoratorIntegrator.class);
    Multibinder<FrameDecorator> frameDecoratorBinder = Multibinder
        .newSetBinder(binder(), FrameDecorator.class);
    frameDecoratorBinder.addBinding().to(ResizeFrameDecorator.class);
    bind(ConnectionParser.class);
    bind(ControllerSlicer.class);

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
