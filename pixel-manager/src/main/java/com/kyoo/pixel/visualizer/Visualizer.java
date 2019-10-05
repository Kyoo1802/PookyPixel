package com.kyoo.pixel.visualizer;

import com.google.inject.Inject;
import com.kyoo.pixel.data.PixelConnection;
import com.kyoo.pixel.data.PixelController;
import com.kyoo.pixel.visualizer.components.capturer.Capturer;
import com.kyoo.pixel.visualizer.components.decorator.ImageDecoratorIntegrator;
import com.kyoo.pixel.visualizer.components.out.FrameOutPublisher;
import com.kyoo.pixel.visualizer.components.out.WifiSubscriber;
import com.kyoo.pixel.visualizer.components.parser.ConnectionParser;
import com.kyoo.pixel.visualizer.components.serializer.ControllerSerializer;
import com.kyoo.pixel.visualizer.data.ControllerLedStrips;
import com.kyoo.pixel.visualizer.data.ImageFrame;
import com.kyoo.pixel.visualizer.data.SerializedFrame;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class Visualizer {

  private Capturer capturer;
  private ImageDecoratorIntegrator imageDecoratorIntegrator;
  private ConnectionParser connectionParser;
  private ControllerSerializer controllerSerializer;
  private FrameOutPublisher frameOutPublisher;
  private WifiSubscriber wifiSubscriber;

  @Inject
  public Visualizer(Capturer capturer, ImageDecoratorIntegrator imageDecoratorIntegrator,
      ConnectionParser connectionParser, ControllerSerializer controllerSerializer,
      FrameOutPublisher frameOutPublisher, WifiSubscriber wifiSubscriber) {
    this.capturer = capturer;
    this.imageDecoratorIntegrator = imageDecoratorIntegrator;
    this.connectionParser = connectionParser;
    this.controllerSerializer = controllerSerializer;
    this.frameOutPublisher = frameOutPublisher;
    this.wifiSubscriber = wifiSubscriber;
  }

  public void capture() {
    Optional<ImageFrame> optionalImageFrame = capturer.getImageFrame();
    if (optionalImageFrame.isEmpty()) {
      return;
    }

    ImageFrame imageFrame = optionalImageFrame.get();
    BufferedImage newImage =
        imageDecoratorIntegrator.decorate(imageFrame.getBufferedImage());
    imageFrame.setBufferedImage(newImage);

    ControllerLedStrips ledStrips = connectionParser.parse(imageFrame);
    SerializedFrame serializedFrame = controllerSerializer.serialize(ledStrips);

    frameOutPublisher.publish(serializedFrame);
  }

  public void updateConnection(PixelConnection connection) {
    connectionParser.update(connection);
    imageDecoratorIntegrator.update(connection);

  }

  public void updateController(PixelController controller) {
    controllerSerializer.update(controller);
    wifiSubscriber.update(controller);
  }
}
