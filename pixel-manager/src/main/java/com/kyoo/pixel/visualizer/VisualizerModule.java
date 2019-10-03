package com.kyoo.pixel.visualizer;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.kyoo.pixel.visualizer.components.capturer.Capturer;
import com.kyoo.pixel.visualizer.components.capturer.FrameStats;
import com.kyoo.pixel.visualizer.components.capturer.ScreenCapturer;
import com.kyoo.pixel.visualizer.components.decorator.ImageDecorator;
import com.kyoo.pixel.visualizer.components.decorator.ImageDecoratorIntegrator;
import com.kyoo.pixel.visualizer.components.decorator.ResizeImageDecorator;
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

    bind(ImageDecoratorIntegrator.class);
    Multibinder<ImageDecorator> frameDecoratorBinder = Multibinder
        .newSetBinder(binder(), ImageDecorator.class);
    frameDecoratorBinder.addBinding().to(ResizeImageDecorator.class);
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
