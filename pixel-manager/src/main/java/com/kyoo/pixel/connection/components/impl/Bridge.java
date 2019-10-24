package com.kyoo.pixel.connection.components.impl;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.utils.DrawUtils;
import java.awt.Dimension;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;

public class Bridge implements SelectableComponent {

  private long id;
  private ConnectionComponent startComponent;
  private ConnectionComponent endComponent;

  public Bridge(long id, ConnectionComponent startComponent, ConnectionComponent endComponent) {
    this.id = id;
    this.startComponent = startComponent;
    this.endComponent = endComponent;
  }

  @Override
  public SelectedSide select(int x, int y) {
    return SelectedSide.NONE; // TODO (Kyoo): Implement bridge selection
  }

  @Override
  public SelectedSide getSelectedSide() {
    return SelectedSide.NONE;
  }

  @Override
  public void move(Point movement) {

  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Point getStartIdxPosition() {
    return startComponent.getEndIdxPosition();
  }

  @Override
  public Point getEndIdxPosition() {
    return endComponent.getStartIdxPosition();
  }

  @Override
  public Dimension getSize() {
    return new Dimension(getEndIdxPosition().x - getStartIdxPosition().x + 1,
        getEndIdxPosition().y - getStartIdxPosition().y + 1);
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.BRIDGE;
  }

  @Override
  public String description() {
    return String.format("EMPTY");
  }

  @Override
  public void unSelect() {
  }

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    DrawUtils.drawBridge(gc, properties, this);
    DrawUtils.drawComponentSelection(gc, properties, this);
  }
}
