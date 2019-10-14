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
  public boolean execute() {
    if (!isValidCommand()) {
      return false;
    }
    SquarePanel squarePanel = new SquarePanel(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    model.addComponent(squarePanel);
    model.setSelectedComponent(Optional.of(squarePanel));
    log.debug("Draw Square Panel triggered %s-%s", request.getStartIdxPosition(),
        request.getEndIdxPosition());
    return true;
  }

  private boolean isValidCommand() {
    return request.getEndIdxPosition().x - request.getStartIdxPosition().x + 1 > 0
        && request.getEndIdxPosition().y - request.getStartIdxPosition().y + 1 > 0;
  }

  @Override
  public void undo() {
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Draw Delete Square Panel triggered %s-%s", request.getCommandType(),
        request.getId());
  }
}
