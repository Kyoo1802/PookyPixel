package com.kyoo.pixel.data.connection.actions;

import com.google.inject.Inject;
import com.kyoo.pixel.data.connection.SquarePanel;
import com.kyoo.pixel.data.connection.actions.ConnectionActionRequest.DrawPanelActionRequest;
import com.kyoo.pixel.views.connection.ConnectionModel;
import java.util.Optional;

public final class DrawPanelAction implements ConnectionAction {

  private ConnectionModel connectionModel;
  private DrawPanelActionRequest request;

  @Inject
  public DrawPanelAction(ConnectionModel connectionModel, DrawPanelActionRequest request) {
    this.connectionModel = connectionModel;
    this.request = request;
  }

  @Override
  public void execute() {
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    connectionModel.getCreatedComponents().addComponent(squarePanel);
    connectionModel.setSelectedComponent(Optional.of(squarePanel));
  }

  @Override
  public void undo() {
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    connectionModel.getCreatedComponents()
        .removeComponent(request.getComponentType(), request.getId());
    connectionModel.setSelectedComponent(Optional.empty());
  }
}
