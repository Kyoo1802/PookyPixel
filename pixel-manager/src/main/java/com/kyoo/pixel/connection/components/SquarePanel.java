package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.Data;

@Data
public final class SquarePanel implements ConnectionComponent {

  private Point startIdxPosition;
  private Point endIdxPosition;
  private LinkedHashMap<Point, Led> leds;
  private long id;

  public SquarePanel(long id, Point startIdxPosition, Point endIdxPosition) {
    this.id = id;
    this.leds = new LinkedHashMap<>();
    this.startIdxPosition = startIdxPosition;
    this.endIdxPosition = endIdxPosition;
    createLeds();
  }

  @Override
  public boolean intersects(int x, int y) {
    return startIdxPosition.x <= x && x <= endIdxPosition.x && startIdxPosition.y <= y
        && y <= endIdxPosition.y;
  }

  @Override
  public boolean internalSelect(int x, int y) {
    return leds.containsKey(new Point(x, y));
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.SQUARE_PANEL;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public CreationType getCreationType() {
    return CreationType.TWO_POINTS;
  }

  public void createLeds() {
    for (int i = startIdxPosition.y, pair = 0; i <= endIdxPosition.y; i++, pair++) {
      for (int j = startIdxPosition.x; j <= endIdxPosition.x; j++) {
        int tmpJ = pair % 2 == 0 ? j : startIdxPosition.x + endIdxPosition.x - j;
        Point ledPoint = new Point(tmpJ, i);
        this.leds.put(ledPoint, new Led(ledPoint, this));
      }
    }
  }

  public long getId() {
    return id;
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
    for (int i = startIdxPosition.y, pair = 0; i <= newHeight; i++, pair++) {
      for (int j = startIdxPosition.x; j <= newWidth; j++) {
        int tmpJ = pair % 2 == 0 ? j : startIdxPosition.x + newWidth - j;
        Point ledPoint = new Point(tmpJ, i);
        if (this.leds.containsKey(ledPoint)) {
          newLeds.put(leds.get(ledPoint).getIdxPosition(), this.leds.get(ledPoint));
        } else {
          newLeds.put(ledPoint, new Led(ledPoint, this));
        }
      }
    }
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
  public String description() {
    int w = getEndIdxPosition().x - getStartIdxPosition().x + 1;
    int h = getEndIdxPosition().y - getStartIdxPosition().y + 1;
    return String.format("[%d, %d] = %d", w, h, w * h);
  }
}
