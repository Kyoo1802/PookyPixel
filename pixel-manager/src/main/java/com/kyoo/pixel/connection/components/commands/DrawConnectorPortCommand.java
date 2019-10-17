package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ConnectionPort;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawConnectorPortCommandRequest;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawConnectorPortCommand implements ConnectionCommand {

  private ConnectionModel model;
  private DrawConnectorPortCommandRequest request;

  public DrawConnectorPortCommand(ConnectionModel model, DrawConnectorPortCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    ConnectionPort port = new ConnectionPort(request.getId(), request.getStartIdxPosition(),
        request.getEndIdxPosition());
    model.addComponent(port);
    model.setSelectedComponent(Optional.of(port));
    log.debug("Draw Connection Port triggered %s", request.getStartIdxPosition());
    return true;
  }

  @Override
  public void undo() {
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Draw Delete Connection Port triggered %s-%s", request.getCommandType(),
        request.getId());
  }
}
