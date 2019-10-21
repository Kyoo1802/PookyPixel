package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.Data;

@Data
public final class SquarePanel implements LedComponent, ScalableComponent {

  private Point startIdxPosition;
  private Point endIdxPosition;
  private LinkedHashMap<Point, Led> leds;
  private long id;
  private Led first;
  private Led last;
  private Optional<Bridge> startBridge;
  private Optional<Bridge> endBridge;

  public SquarePanel(long id, Point startIdxPosition, Point endIdxPosition) {
    this.id = id;
    this.leds = new LinkedHashMap<>();
    this.startIdxPosition = startIdxPosition;
    this.endIdxPosition = endIdxPosition;
    this.startBridge = Optional.empty();
    this.endBridge = Optional.empty();
    createLeds();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public boolean intersects(int x, int y) {
    return startIdxPosition.x <= x && x <= endIdxPosition.x && startIdxPosition.y <= y
        && y <= endIdxPosition.y;
  }

  public void createLeds() {
    for (int i = startIdxPosition.y, pair = 0; i <= endIdxPosition.y; i++, pair++) {
      for (int j = startIdxPosition.x; j <= endIdxPosition.x; j++) {
        int tmpJ = pair % 2 == 0 ? j : startIdxPosition.x + endIdxPosition.x - j;
        Point ledPoint = new Point(tmpJ, i);
        Led led = new Led(ledPoint, this);
        this.leds.put(ledPoint, led);
        if (first == null) {
          first = led;
        }
        last = led;

      }
    }
  }

  public Point getStartIdxPosition() {
    return startIdxPosition;
  }

  public Point getEndIdxPosition() {
    return endIdxPosition;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(endIdxPosition.x - startIdxPosition.x + 1,
        endIdxPosition.y - startIdxPosition.y + 1);
  }

  @Override
  public void addDimension(Dimension addDimension) {
    LinkedHashMap<Point, Led> newLeds = new LinkedHashMap<>();
    int newWidth = endIdxPosition.x + addDimension.width;
    int newHeight = endIdxPosition.y + addDimension.height;
    Led tempFirst = null;
    Led tempLast = null;
    for (int i = startIdxPosition.y, pair = 0; i <= newHeight; i++, pair++) {
      for (int j = startIdxPosition.x; j <= newWidth; j++) {
        int tmpJ = pair % 2 == 0 ? j : startIdxPosition.x + newWidth - j;
        Point ledPoint = new Point(tmpJ, i);
        Led led;
        if (this.leds.containsKey(ledPoint)) {
          led = this.leds.get(ledPoint);
          newLeds.put(led.getStartIdxPosition(), led);
        } else {
          led = new Led(ledPoint, this);
          newLeds.put(ledPoint, led);
        }
        if (tempFirst == null) {
          tempFirst = led;
        }
        tempLast = led;
      }
    }
    this.first = tempFirst;
    this.last = tempLast;
    this.leds.clear();
    this.leds.putAll(newLeds);
    this.endIdxPosition.setLocation(newWidth, newHeight);
  }

  @Override
  public ComponentSide scaleIntersection(int x, int y) {
    if (startIdxPosition.x <= x && x <= startIdxPosition.x) {
      if (startIdxPosition.y <= y && y <= startIdxPosition.y) {
        return ComponentSide.UPPER_LEFT;
      } else if (endIdxPosition.y <= y && y <= endIdxPosition.y) {
        return ComponentSide.BOTTOM_LEFT;
      } else if (startIdxPosition.y <= y && y <= endIdxPosition.y) {
        return ComponentSide.LEFT;
      }
    } else if (endIdxPosition.x <= x && x <= endIdxPosition.x) {
      if (startIdxPosition.y <= y && y <= startIdxPosition.y) {
        return ComponentSide.UPPER_RIGHT;
      } else if (endIdxPosition.y <= y && y <= endIdxPosition.y) {
        return ComponentSide.BOTTOM_RIGHT;
      } else if (startIdxPosition.y <= y && y <= endIdxPosition.y) {
        return ComponentSide.RIGHT;
      }
    } else if (startIdxPosition.x <= x && x <= endIdxPosition.x) {
      if (startIdxPosition.y <= y && y == startIdxPosition.y) {
        return ComponentSide.UPPER;
      } else if (endIdxPosition.y <= y && y == endIdxPosition.y) {
        return ComponentSide.BOTTOM;
      }
    }
    return ComponentSide.NONE;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.SQUARE_PANEL;
  }

  @Override
  public void setStartBridge(@Nullable Bridge bridge) {
    startBridge = Optional.ofNullable(bridge);
  }

  @Override
  public void setEndBridge(@Nullable Bridge bridge) {
    endBridge = Optional.ofNullable(bridge);
  }

  @Override
  public String description() {
    int w = getEndIdxPosition().x - getStartIdxPosition().x + 1;
    int h = getEndIdxPosition().y - getStartIdxPosition().y + 1;
    return String.format("[%d, %d] = %d", w, h, w * h);
  }

  @Override
  public Led getFirstLed() {
    return first;
  }

  @Override
  public Led getLastLed() {
    return last;
  }
}
