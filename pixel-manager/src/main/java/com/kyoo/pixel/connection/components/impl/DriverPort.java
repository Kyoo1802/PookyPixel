package com.kyoo.pixel.connection.components.impl;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public final class DriverPort implements ConnectionComponent {

  @Getter
  private final ComponentType componentType;
  @Getter
  private long id;
  @Getter
  private Point startIdxPosition;
  @Getter
  private Point endIdxPosition;
  @Getter
  @Setter
  private SelectedSide selectedSide;
  @Getter
  @Setter
  private Optional<ConnectionComponent> previousComponent;
  @Getter
  @Setter
  private Optional<ConnectionComponent> nextComponent;

  public DriverPort(long id, Point startIdxPosition, Dimension size) {
    this.id = id;
    this.startIdxPosition = startIdxPosition;
    this.endIdxPosition = new Point(
        startIdxPosition.x + size.width - 1, startIdxPosition.y + size.height - 1);
    this.componentType = ComponentType.DRIVER_PORT;
  }

  @Override
  public SelectedSide hasSelection(int x, int y) {
    Dimension size = getSize();
    return
        startIdxPosition.x <= x && x <= startIdxPosition.x + size.width && startIdxPosition.y <= y
            && y <= startIdxPosition.y + size.height ? SelectedSide.CENTER :
            SelectedSide.NONE;
  }


  @Override
  public Dimension getSize() {
    return new Dimension(
        endIdxPosition.x - startIdxPosition.x + 1, endIdxPosition.y - startIdxPosition.y + 1);
  }

  @Override
  public void move(Point movement) {
    startIdxPosition.setLocation(startIdxPosition.x + movement.x,
        startIdxPosition.y + movement.y);
    endIdxPosition.setLocation(endIdxPosition.x + movement.x,
        endIdxPosition.y + movement.y);
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
    DrawComponentUtils.drawDriverPort(gc, properties, this);
    DrawComponentUtils.drawComponentSelection(gc, properties, this);
  }
}
