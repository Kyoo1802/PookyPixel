package com.kyoo.pixel.connection.components.impl;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.LedComponent;
import com.kyoo.pixel.connection.components.ScalableComponent;
import com.kyoo.pixel.utils.DrawUtils;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

public final class SquarePanel implements LedComponent, ScalableComponent {

  @Getter
  private final ComponentType componentType;
  @Getter
  private Point startIdxPosition;
  @Getter
  private Point endIdxPosition;
  @Getter
  private long id;
  @Getter
  private Led firstLed;
  @Getter
  private Led lastLed;
  @Setter
  @Getter
  private Optional<Bridge> startBridge;
  @Setter
  @Getter
  private Optional<Bridge> endBridge;
  @Getter
  private SelectedSide selectedSide;

  private LinkedHashMap<Point, Led> leds;

  public SquarePanel(long id, Point startIdxPosition, Point endIdxPosition) {
    this.id = id;
    this.leds = new LinkedHashMap<>();
    this.startIdxPosition = startIdxPosition;
    this.endIdxPosition = endIdxPosition;
    this.startBridge = Optional.empty();
    this.endBridge = Optional.empty();
    this.selectedSide = SelectedSide.NONE;
    this.componentType = ComponentType.SQUARE_PANEL;
    createLeds();
  }

  public Collection<Led> getLeds() {
    return leds.values();
  }

  @Override
  public SelectedSide select(int x, int y) {
    if (startIdxPosition.x <= x && x <= endIdxPosition.x && startIdxPosition.y <= y
        && y <= endIdxPosition.y) {
      if (startIdxPosition.x <= x && x <= startIdxPosition.x) {
        if (startIdxPosition.y <= y && y <= startIdxPosition.y) {
          return setSelectedSide(SelectedSide.UPPER_LEFT);
        } else if (endIdxPosition.y <= y && y <= endIdxPosition.y) {
          return setSelectedSide(SelectedSide.BOTTOM_LEFT);
        } else if (startIdxPosition.y <= y && y <= endIdxPosition.y) {
          return setSelectedSide(SelectedSide.LEFT);
        }
      } else if (endIdxPosition.x <= x && x <= endIdxPosition.x) {
        if (startIdxPosition.y <= y && y <= startIdxPosition.y) {
          return setSelectedSide(SelectedSide.UPPER_RIGHT);
        } else if (endIdxPosition.y <= y && y <= endIdxPosition.y) {
          return setSelectedSide(SelectedSide.BOTTOM_RIGHT);
        } else if (startIdxPosition.y <= y && y <= endIdxPosition.y) {
          return setSelectedSide(SelectedSide.RIGHT);
        }
      } else if (startIdxPosition.x <= x && x <= endIdxPosition.x) {
        if (startIdxPosition.y <= y && y == startIdxPosition.y) {
          return setSelectedSide(SelectedSide.UPPER);
        } else if (endIdxPosition.y <= y && y == endIdxPosition.y) {
          return setSelectedSide(SelectedSide.BOTTOM);
        }
      }
      return setSelectedSide(SelectedSide.CENTER);
    }
    return setSelectedSide(SelectedSide.NONE);
  }

  private SelectedSide setSelectedSide(SelectedSide selectedSide) {
    return this.selectedSide = selectedSide;
  }

  public void createLeds() {
    for (int i = startIdxPosition.y, pair = 0; i <= endIdxPosition.y; i++, pair++) {
      for (int j = startIdxPosition.x; j <= endIdxPosition.x; j++) {
        int tmpJ = pair % 2 == 0 ? j : startIdxPosition.x + endIdxPosition.x - j;
        Point ledPoint = new Point(tmpJ, i);
        Led led = new Led(ledPoint);
        this.leds.put(ledPoint, led);
        if (firstLed == null) {
          firstLed = led;
        }
        lastLed = led;

      }
    }
  }

  @Override
  public Dimension getSize() {
    return new Dimension(endIdxPosition.x - startIdxPosition.x + 1,
        endIdxPosition.y - startIdxPosition.y + 1);
  }

  @Override
  public void move(Point movement) {

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
          led = new Led(ledPoint);
          newLeds.put(ledPoint, led);
        }
        if (tempFirst == null) {
          tempFirst = led;
        }
        tempLast = led;
      }
    }
    this.firstLed = tempFirst;
    this.lastLed = tempLast;
    this.leds.clear();
    this.leds.putAll(newLeds);
    this.endIdxPosition.setLocation(newWidth, newHeight);
  }

  @Override
  public String description() {
    int w = getEndIdxPosition().x - getStartIdxPosition().x + 1;
    int h = getEndIdxPosition().y - getStartIdxPosition().y + 1;
    return String.format("[%d, %d] = %d", w, h, w * h);
  }

  @Override
  public void unSelect() {
    selectedSide = SelectedSide.NONE;
  }

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    DrawUtils.drawLedComponent(gc, properties, this);
    DrawUtils.drawComponentSelection(gc, properties, this);
  }
}