package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import java.awt.Point;
import java.util.Map;
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
  public void execute() {
    Map<Long, ConnectionComponent> componentsByType =
        model.getCreatedComponentsManager().all().get(request.getComponentType());
    if (componentsByType != null && componentsByType.containsKey(request.getId())) {
      Point movement = new Point(request.getEndIdxPosition().x - request.getStartIdxPosition().x,
          request.getEndIdxPosition().y + request.getStartIdxPosition().y);
      move(componentsByType.get(request.getId()), movement);
    }
    log.debug("No selection for move %s", request);
    model.setSelectedComponent(Optional.empty());
  }

  private void move(ConnectionComponent component, Point movement) {
    component.getStartIdxPosition().setLocation(component.getStartIdxPosition().x + movement.x,
        component.getStartIdxPosition().y + movement.y);
  }

  @Override
  public void undo() {
    model.setSelectedComponent(Optional.empty());
  }
}
