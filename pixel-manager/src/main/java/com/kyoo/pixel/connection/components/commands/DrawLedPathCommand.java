package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.impl.LedPath;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawLedPathCommand implements ConnectionCommand {

  private ConnectionModel model;
  private DrawLedPathCommandRequest request;

  public DrawLedPathCommand(ConnectionModel model, DrawLedPathCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    LedPath ledPath = new LedPath(request.getId(), request.getIdxPositions());
    model.addComponent(ledPath);
    model.setSelectedComponent(Optional.of(ledPath));
    log.debug("Create Led Path [%s, %d]", request.getCommandType(), request.getId());
    return true;
  }

  @Override
  public void undo() {
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Delete Led Path [%s, %d]", request.getCommandType(), request.getId());
  }
}
