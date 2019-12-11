package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.utils.draw.DrawComponentUtils;
import com.kyoo.pixel.views.common.paint.ScalableObject;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.commands.CreateSquarePanelCommand.CreateSquarePanelCommandRequest;
import com.kyoo.pixel.views.stage.fixture.components.led.SquarePanel;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public final class InteractiveSquarePanel extends InteractiveComponent implements ScalableObject {

  public InteractiveSquarePanel(SquarePanel squarePanel) {
    super(squarePanel);
  }

  public static InteractiveSquarePanel from(CreateSquarePanelCommandRequest request) {
    return from(
        new SquarePanel(
            request.getId(), request.getStartIdxPosition(), request.getEndIdxPosition()));
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
