package com.kyoo.pixel.visualizer;

import com.google.inject.Inject;
import com.kyoo.pixel.visualizer.components.Capturer;
import com.kyoo.pixel.visualizer.components.ConnectionParser;
import com.kyoo.pixel.visualizer.components.ControllerSlicer;
import com.kyoo.pixel.visualizer.components.EditDecorator;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import com.kyoo.pixel.visualizer.pubsub.FramePublisher;
import com.kyoo.pixel.visualizer.pubsub.WifiSubscriber;

public final class Visualizer {

  private Capturer capturer;
  private EditDecorator editDecorator;
  private ConnectionParser connectionParser;
  private ControllerSlicer controllerSlicer;
  private FramePublisher framePublisher;
  private WifiSubscriber wifiSubscriber;

  @Inject
  public Visualizer(Capturer capturer, EditDecorator editDecorator,
      ConnectionParser connectionParser, ControllerSlicer controllerSlicer,
      FramePublisher framePublisher, WifiSubscriber wifiSubscriber) {
    this.capturer = capturer;
    this.editDecorator = editDecorator;
    this.connectionParser = connectionParser;
    this.controllerSlicer = controllerSlicer;
    this.framePublisher = framePublisher;
    this.wifiSubscriber = wifiSubscriber;
  }

  public void capture() {
    PixelFrame frame = capturer.getFrame();
    frame = editDecorator.edit(frame);
    frame = connectionParser.parse(frame);
    frame = controllerSlicer.slice(frame);
    framePublisher.publish(frame);
  }

  public void updateConnection(PixelConnection connection) {
    connectionParser.update(connection);

  }

  public void updateController(PixelController controller) {
    controllerSlicer.update(controller);
    wifiSubscriber.update(controller);
  }
}
