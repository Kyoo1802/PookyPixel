package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionModel.TransformationAction;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.SelectCommandRequest;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SelectCommand implements ConnectionCommand {

  private ConnectionModel model;
  private SelectCommandRequest request;

  public SelectCommand(ConnectionModel model, SelectCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    for (Map<Long, SelectableComponent> components :
        model.getCreatedComponentsManager().getComponents().values()) {
      for (SelectableComponent component : components.values()) {
        if (component
            .intersects(request.getSelectIdxPosition().x, request.getSelectIdxPosition().y)) {
          model.setSelectedComponent(Optional.of(component));
          model.setTransformationActionState(TransformationAction.MOVE);
          log.debug("Selection triggered %s", request.getSelectIdxPosition());
          return true;
        }
      }
    }

    log.debug("No selection triggered %s", request.getSelectIdxPosition());
    model.setSelectedComponent(Optional.empty());
    model.setActiveCommandRequest(Optional.empty());
    return false;
  }

  @Override
  public void undo() {
    model.setSelectedComponent(Optional.empty());
  }
}
