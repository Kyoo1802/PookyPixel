package com.kyoo.pixel.fixtures.components.led;

import com.kyoo.pixel.fixtures.components.ComponentType;
import com.kyoo.pixel.fixtures.components.LedComponent;
import java.awt.Dimension;
import java.awt.Point;

public final class SquarePanel extends LedComponent {

  public SquarePanel(long id, Point position, Point endPosition) {
    super(id, ComponentType.SQUARE_PANEL);
    this.position = position;
    this.size = new Dimension(endPosition.x - this.position.x + 1,
        endPosition.y - this.position.y + 1);

    // Create leds
    for (int i = 0; i < size.height; i++) {
      for (int j = 0; j < size.width; j++) {
        int tmpJ = i % 2 == 0 ? j : size.width - j - 1;
        Point ledPoint = new Point(tmpJ, i);
        Led led = new Led(id, ledPoint);
        this.leds.put(ledPoint, led);
        if (firstLed == null) {
          firstLed = led;
        }
        lastLed = led;
      }
    }
  }

  public Point getEndPosition() {
    return new Point(position.x + size.width - 1, position.y + size.height - 1);
  }
}
