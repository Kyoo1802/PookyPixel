package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawSquarePanelCommand implements ConnectionCommand {

  private ConnectionModel model;
  private DrawSquarePanelCommandRequest request;

  public DrawSquarePanelCommand(ConnectionModel model, DrawSquarePanelCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public void execute() {
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    model.addComponent(squarePanel);
    model.setSelectedComponent(Optional.of(squarePanel));
    log.debug("Draw Square Panel triggered %s-%s", request.getStartIdxPosition(),
        request.getEndIdxPosition());
  }

  @Override
  public void undo() {
    model.removeComponent(request.getComponentType(), request.getId());
    log.debug("Draw Delete Square Panel triggered %s-%s", request.getComponentType(),
        request.getId());
  }
}
