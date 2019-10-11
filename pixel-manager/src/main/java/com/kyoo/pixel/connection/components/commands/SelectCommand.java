package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.SelectCommandRequest;
import java.util.Map;
import java.util.Optional;

public final class SelectCommand implements ConnectionCommand {

  private ConnectionModel connectionModel;
  private SelectCommandRequest request;

  public SelectCommand(ConnectionModel connectionModel, SelectCommandRequest request) {
    this.connectionModel = connectionModel;
    this.request = request;
  }

  @Override
  public void execute() {
    if (connectionModel.getSelectedComponent().isPresent()) {
      return;
    }
    for (Map<Long, ConnectionComponent> components :
        connectionModel.getCreatedComponents().all().values()) {
      for (ConnectionComponent component : components.values()) {
        if (component.intersects(request.getIdxPosition().x, request.getIdxPosition().y)) {
          connectionModel.setSelectedComponent(Optional.of(component));
          return;
        }
      }
    }
    connectionModel.setSelectedComponent(Optional.empty());
  }

  @Override
  public void undo() {
    connectionModel.setSelectedComponent(Optional.empty());
  }
}
