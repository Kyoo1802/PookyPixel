package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.LedBridge;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedBridgeCommandRequest;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawLedBridgeCommand implements ConnectionCommand {

  private ConnectionModel model;
  private DrawLedBridgeCommandRequest request;

  public DrawLedBridgeCommand(ConnectionModel model, DrawLedBridgeCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    LedBridge port = new LedBridge(request.getId(), request.getStartComponent(),
        request.getEndComponent());
    request.getStartComponent().setStartBridge(Optional.of(port));
    request.getEndComponent().setEndBridge(Optional.of(port));
    model.addComponent(port);
    model.setSelectedComponent(Optional.of(port));
    log.debug("Draw Connection Port triggered %s", request);
    return true;
  }

  @Override
  public void undo() {
    request.getStartComponent().setStartBridge(Optional.empty());
    request.getEndComponent().setEndBridge(Optional.empty());
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Draw Delete Connection Port triggered %s-%s", request.getCommandType(),
        request.getId());
  }
}
