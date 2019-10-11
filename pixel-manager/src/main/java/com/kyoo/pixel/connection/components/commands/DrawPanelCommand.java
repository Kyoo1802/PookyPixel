package com.kyoo.pixel.connection.components.commands;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawPanelCommandRequest;
import java.util.Optional;

public final class DrawPanelCommand implements ConnectionCommand {

  private ConnectionModel connectionModel;
  private DrawPanelCommandRequest request;

  @Inject
  public DrawPanelCommand(ConnectionModel connectionModel, DrawPanelCommandRequest request) {
    this.connectionModel = connectionModel;
    this.request = request;
  }

  @Override
  public void execute() {
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    connectionModel.getCreatedComponentsManager().addComponent(squarePanel);
    connectionModel.setSelectedComponent(Optional.of(squarePanel));
  }

  @Override
  public void undo() {
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    connectionModel.getCreatedComponentsManager()
        .removeComponent(request.getComponentType(), request.getId());
    connectionModel.setSelectedComponent(Optional.empty());
  }
}
