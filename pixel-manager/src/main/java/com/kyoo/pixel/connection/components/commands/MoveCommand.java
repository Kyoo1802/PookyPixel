package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.LedPath;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import java.awt.Point;
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
    return doMove(false);
  }

  @Override
  public void undo() {
    doMove(true);
  }

  private boolean doMove(boolean isInverse) {
    Optional<SelectableComponent> component =
        model.getCreatedComponentsManager().getComponent(request.getTypeToMove(),
            request.getIdToMove());

    if (component.isPresent()) {
      Point movement = new Point(request.getEndIdxPosition().x - request.getStartIdxPosition().x,
          request.getEndIdxPosition().y - request.getStartIdxPosition().y);
      movement = isInverse ? invert(movement) : movement;
      move(component.get(), movement);
      return true;
    }
    log.debug("No component selected for move %s", request);
    model.setSelectedComponent(Optional.empty());
    return false;
  }

  private Point invert(Point movement) {
    movement.x = -movement.x;
    movement.y = -movement.y;
    return movement;
  }

  private void move(SelectableComponent component, Point movement) {
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
        break;
      case DRIVER_PORT:
        component.getStartIdxPosition().setLocation(component.getStartIdxPosition().x + movement.x,
            component.getStartIdxPosition().y + movement.y);
        component.getEndIdxPosition().setLocation(component.getEndIdxPosition().x + movement.x,
            component.getEndIdxPosition().y + movement.y);
        break;
      case LED_PATH:
        LedPath lp = (LedPath) component;
        for (Led led : lp.getLeds()) {
          led.getStartIdxPosition().setLocation(led.getStartIdxPosition().x + movement.x,
              led.getStartIdxPosition().y + movement.y);
        }
        component.getStartIdxPosition().setLocation(component.getStartIdxPosition().x + movement.x,
            component.getStartIdxPosition().y + movement.y);
        component.getEndIdxPosition().setLocation(component.getEndIdxPosition().x + movement.x,
            component.getEndIdxPosition().y + movement.y);
        break;
      default:
        log.error("Invalid movement for component: " + component.getComponentType());
    }
  }
}
