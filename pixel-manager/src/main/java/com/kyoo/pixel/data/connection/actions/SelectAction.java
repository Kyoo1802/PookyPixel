package com.kyoo.pixel.data.connection.actions;

import com.kyoo.pixel.data.connection.ConnectionComponent;
import com.kyoo.pixel.data.connection.actions.ConnectionActionRequest.SelectActionRequest;
import com.kyoo.pixel.views.connection.ConnectionModel;
import java.util.Map;
import java.util.Optional;

public final class SelectAction implements ConnectionAction {

  private ConnectionModel connectionModel;
  private SelectActionRequest request;

  public SelectAction(ConnectionModel connectionModel, SelectActionRequest request) {
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
