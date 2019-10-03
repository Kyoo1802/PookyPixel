package com.kyoo.pixel.visualizer;

import com.google.inject.Inject;
import com.kyoo.pixel.data.PixelConnection;
import com.kyoo.pixel.data.PixelController;
import com.kyoo.pixel.visualizer.components.capturer.Capturer;
import com.kyoo.pixel.visualizer.components.parser.ConnectionParser;
import com.kyoo.pixel.visualizer.components.serializer.ControllerSerializer;
import com.kyoo.pixel.visualizer.components.decorator.FrameDecoratorIntegrator;
import com.kyoo.pixel.visualizer.data.FrameData;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import com.kyoo.pixel.visualizer.data.RgbLedStrips;
import com.kyoo.pixel.visualizer.components.out.FrameOutPublisher;
import com.kyoo.pixel.visualizer.components.out.WifiSubscriber;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class Visualizer {

  private Capturer capturer;
  private FrameDecoratorIntegrator frameDecoratorIntegrator;
  private ConnectionParser connectionParser;
  private ControllerSerializer controllerSerializer;
  private FrameOutPublisher frameOutPublisher;
  private WifiSubscriber wifiSubscriber;

  @Inject
  public Visualizer(Capturer capturer, FrameDecoratorIntegrator frameDecoratorIntegrator,
      ConnectionParser connectionParser, ControllerSerializer controllerSerializer,
      FrameOutPublisher frameOutPublisher, WifiSubscriber wifiSubscriber) {
    this.capturer = capturer;
    this.frameDecoratorIntegrator = frameDecoratorIntegrator;
    this.connectionParser = connectionParser;
    this.controllerSerializer = controllerSerializer;
    this.frameOutPublisher = frameOutPublisher;
    this.wifiSubscriber = wifiSubscriber;
  }

  public void capture() {
    Optional<PixelFrame> optionalFrameImage = capturer.getFrame();
    if(optionalFrameImage.isEmpty()){
      return;
    }

    PixelFrame pixelFrame = optionalFrameImage.get();
    BufferedImage newframeImage =
        frameDecoratorIntegrator.decorate(pixelFrame.getBufferedImage());
    pixelFrame.setBufferedImage(newframeImage);

    RgbLedStrips ledStrips = connectionParser.parse(pixelFrame);
    FrameData frameData = controllerSerializer.serialize(ledStrips);

    frameOutPublisher.publish(frameData);
  }

  public void updateConnection(PixelConnection connection) {
    connectionParser.update(connection);
    frameDecoratorIntegrator.update(connection);

  }

  public void updateController(PixelController controller) {
    controllerSerializer.update(controller);
    wifiSubscriber.update(controller);
  }
}
