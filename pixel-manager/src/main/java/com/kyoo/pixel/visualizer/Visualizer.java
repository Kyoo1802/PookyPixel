package com.kyoo.pixel.visualizer;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.components.Capturer;
import com.kyoo.pixel.visualizer.components.ConnectionParser;
import com.kyoo.pixel.visualizer.components.ControllerSlicer;
import com.kyoo.pixel.visualizer.components.FrameDecoratorIntegrator;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import com.kyoo.pixel.visualizer.pubsub.FramePublisher;
import com.kyoo.pixel.visualizer.pubsub.WifiSubscriber;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class Visualizer {

  private Capturer capturer;
  private FrameDecoratorIntegrator frameDecoratorIntegrator;
  private ConnectionParser connectionParser;
  private ControllerSlicer controllerSlicer;
  private FramePublisher framePublisher;
  private WifiSubscriber wifiSubscriber;

  @Inject
  public Visualizer(Capturer capturer, FrameDecoratorIntegrator frameDecoratorIntegrator,
      ConnectionParser connectionParser, ControllerSlicer controllerSlicer,
      FramePublisher framePublisher, WifiSubscriber wifiSubscriber) {
    this.capturer = capturer;
    this.frameDecoratorIntegrator = frameDecoratorIntegrator;
    this.connectionParser = connectionParser;
    this.controllerSlicer = controllerSlicer;
    this.framePublisher = framePublisher;
    this.wifiSubscriber = wifiSubscriber;
  }

  public void capture() {
    Optional<BufferedImage> img = capturer.getFrame();
    img = frameDecoratorIntegrator.decorate(img);

    Optional<PixelFrame> frame = connectionParser.parse(img);
    frame = controllerSlicer.slice(frame);
    framePublisher.publish(frame);
  }

  public void updateConnection(PixelConnection connection) {
    connectionParser.update(connection);
    frameDecoratorIntegrator.update(connection);

  }

  public void updateController(PixelController controller) {
    controllerSlicer.update(controller);
    wifiSubscriber.update(controller);
  }
}
