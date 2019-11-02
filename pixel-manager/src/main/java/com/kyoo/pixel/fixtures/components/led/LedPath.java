package com.kyoo.pixel.fixtures.components.led;

import com.kyoo.pixel.fixtures.components.ComponentType;
import com.kyoo.pixel.fixtures.components.LedComponent;
import java.awt.Point;
import java.util.Collection;

public final class LedPath extends LedComponent {

  public LedPath(long id, Collection<Point> ledPositions) {
    super(id, ComponentType.LED_PATH);

    // Compute start and last position
    this.position = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point lastPosition = new Point(0, 0);
    for (Point p : ledPositions) {
      this.position.x = Math.min(p.x, position.x);
      this.position.y = Math.min(p.y, position.y);
      lastPosition.x = Math.max(p.x, lastPosition.x);
      lastPosition.y = Math.max(p.y, lastPosition.y);
    }

    // Compute relative positions
    for (Point p : ledPositions) {
      Point relativeP = new Point(p.x - position.x, p.y - position.y);
      Led newLed = new Led(id, relativeP);
      if (firstLed == null) {
        firstLed = newLed;
      }
      lastLed = newLed;
      this.leds.put(p, newLed);
    }
  }

  @Override
  public int availableInputs() {
    return previousComponentKey.isPresent() ? 0 : 1;
  }

  @Override
  public int availableOutputs() {
    return nextComponentKey.isPresent() ? 0 : 1;
  }
}
