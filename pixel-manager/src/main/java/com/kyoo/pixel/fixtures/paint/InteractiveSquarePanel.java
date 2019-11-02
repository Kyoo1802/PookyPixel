package com.kyoo.pixel.fixtures.paint;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.commands.CreateSquarePanelCommand.CreateSquarePanelCommandRequest;
import com.kyoo.pixel.fixtures.components.led.SquarePanel;
import com.kyoo.pixel.paint.ScalableObject;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.awt.Dimension;
import javafx.scene.canvas.GraphicsContext;

public final class InteractiveSquarePanel extends InteractiveComponent implements
    ScalableObject {

  public InteractiveSquarePanel(SquarePanel squarePanel) {
    super(squarePanel);
  }

  public static InteractiveSquarePanel from(CreateSquarePanelCommandRequest request) {
    return from(new SquarePanel(request.getId(),
        request.getStartIdxPosition(),
        request.getEndIdxPosition()));
  }

  public static InteractiveSquarePanel from(SquarePanel squarePanel) {
    return new InteractiveSquarePanel(squarePanel);
  }

  @Override
  public void drawComponent(GraphicsContext gc, FixtureProperties properties) {
    DrawComponentUtils.drawLedComponent(gc, properties, getComponent());
  }

  @Override
  public boolean scale(Dimension scale) {
    return false;
//    LinkedHashMap<Point, Led> newLeds = new LinkedHashMap<>();
//    Dimension d = getComponent().getSize();
//
//    Dimension newDimension = new Dimension(scale.width + d.width,scale.height+d.height);
//    Led tempFirst = null;
//    Led tempLast = null;
//    for (int i = 0, pair = 0; i <= newDimension.height; i++, pair++) {
//      for (int j = 0; j <= newDimension.width; j++) {
//        int tmpJ = pair % 2 == 0 ? j : newDimension.width - j;
//        Point ledPoint = new Point(tmpJ, i);
//        Led led;
//        if (this.leds.containsKey(ledPoint)) {
//          led = this.leds.get(ledPoint);
//          newLeds.put(led.getStartIdxPosition(), led);
//        } else {
//          led = new Led(ledPoint);
//          newLeds.put(ledPoint, led);
//        }
//        if (tempFirst == null) {
//          tempFirst = led;
//        }
//        tempLast = led;
//      }
//    }
//    this.firstLed = tempFirst;
//    this.lastLed = tempLast;
//    this.leds.clear();
//    this.leds.putAll(newLeds);
//    this.endIdxPosition.setLocation(newWidth, newHeight);
  }

  @Override
  public SquarePanel getComponent() {
    return (SquarePanel) super.getComponent();
  }
}