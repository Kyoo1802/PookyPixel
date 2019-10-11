package com.kyoo.pixel.connection.components.commands;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawPanelCommandRequest;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawSquarePanelCommand implements ConnectionCommand {

  private ConnectionModel model;
  private DrawPanelCommandRequest request;

  @Inject
  public DrawSquarePanelCommand(ConnectionModel model, DrawPanelCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public void execute() {
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    model.getCreatedComponentsManager().addComponent(squarePanel);
    model.setSelectedComponent(Optional.of(squarePanel));

    log.debug("Draw Square Panel triggered %s-%s", request.getStartIdxPosition(),
        request.getEndIdxPosition());
  }

  @Override
  public void undo() {
    model.getCreatedComponentsManager().removeComponent(request.getComponentType(),
        request.getId());
    log.debug("Draw Delete Square Panel triggered %s-%s", request.getComponentType(),
        request.getId());
  }
}
