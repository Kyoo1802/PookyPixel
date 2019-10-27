package com.kyoo.pixel.connection.components.impl;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.LedComponent;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

public final class LedPath implements LedComponent {

  @Getter
  private final ComponentType componentType;
  @Getter
  private long id;
  @Getter
  private Led firstLed;
  @Getter
  private Led lastLed;
  @Getter
  @Setter
  private Optional<ConnectionComponent> previousComponent;
  @Getter
  @Setter
  private Optional<ConnectionComponent> nextComponent;
  @Getter
  @Setter
  private SelectedSide selectedSide;
  @Getter
  private Point startIdxPosition;
  @Getter
  private Point endIdxPosition;
  @Getter
  private LinkedHashSet<Led> leds;

  public LedPath(long id, Collection<Led> ledsInput) {
    this.id = id;
    this.leds = new LinkedHashSet<>();
    this.componentType = ComponentType.LED_PATH;
    this.startIdxPosition = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    this.endIdxPosition = new Point(0, 0);
    for (Led led : ledsInput) {
      Point p = led.getStartIdxPosition();
      startIdxPosition.x = Math.min(p.x, startIdxPosition.x);
      startIdxPosition.y = Math.min(p.y, startIdxPosition.y);
      endIdxPosition.x = Math.max(p.x, endIdxPosition.x);
      endIdxPosition.y = Math.max(p.y, endIdxPosition.y);
      Led newLed = new Led(p);
      if (firstLed == null) {
        firstLed = newLed;
      }
      lastLed = newLed;
      this.leds.add(newLed);
    }
  }

  @Override
  public SelectedSide hasSelection(int x, int y) {
    for (Led led : leds) {
      if (led.hasSelection(x, y) != SelectedSide.NONE) {
        return SelectedSide.CENTER;
      }
    }
    return SelectedSide.NONE;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(endIdxPosition.x - startIdxPosition.x + 1,
        endIdxPosition.y - startIdxPosition.y + 1);
  }

  @Override
  public void move(Point movement) {
    for (Led led : leds) {
      led.getStartIdxPosition().setLocation(led.getStartIdxPosition().x + movement.x,
          led.getStartIdxPosition().y + movement.y);
    }
    startIdxPosition.setLocation(startIdxPosition.x + movement.x, startIdxPosition.y + movement.y);
    endIdxPosition.setLocation(endIdxPosition.x + movement.x, endIdxPosition.y + movement.y);
  }

  @Override
  public String description() {
    return String.format("id: %d, size: %s ", id, getSize());
  }

  @Override
  public void unSelect() {
    selectedSide = SelectedSide.NONE;
  }

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    DrawComponentUtils.drawLedComponent(gc, properties, this);
    DrawComponentUtils.drawComponentSelection(gc, properties, this);
  }
}
