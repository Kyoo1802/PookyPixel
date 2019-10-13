package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class MoveCommand implements ConnectionCommand {

  private ConnectionModel model;
  private MovementCommandRequest request;

  public MoveCommand(ConnectionModel model, MovementCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    doMove(false);
    return true;
  }

  @Override
  public void undo() {
    doMove(true);
  }

  private void doMove(boolean isInverse) {
    Map<Long, ConnectionComponent> componentsByType =
        model.getCreatedComponentsManager().all().get(request.getTypeToMove());

    if (componentsByType != null && componentsByType.containsKey(request.getIdToMove())) {
      Point movement = new Point(request.getEndIdxPosition().x - request.getStartIdxPosition().x,
          request.getEndIdxPosition().y - request.getStartIdxPosition().y);
      movement = isInverse? invert(movement) : movement;
      move(componentsByType.get(request.getIdToMove()), movement);
    }
    log.debug("No component selected for move %s", request);
    model.setSelectedComponent(Optional.empty());
  }

  private Point invert(Point movement) {
    movement.x = -movement.x;
    movement.y = -movement.y;
    return movement;
  }

  private void move(ConnectionComponent component, Point movement) {
    switch (component.getComponentType()) {
      case SQUARE_PANEL:
        SquarePanel sp = (SquarePanel) component;
        for (Entry<Point, Led> led : sp.getLeds().entrySet()) {
          led.getKey().setLocation(led.getKey().x + movement.x, led.getKey().y + movement.y);
        }
        component.getStartIdxPosition().setLocation(component.getStartIdxPosition().x + movement.x,
            component.getStartIdxPosition().y + movement.y);
        component.getEndIdxPosition().setLocation(component.getEndIdxPosition().x + movement.x,
            component.getEndIdxPosition().y + movement.y);
    }
  }
}
