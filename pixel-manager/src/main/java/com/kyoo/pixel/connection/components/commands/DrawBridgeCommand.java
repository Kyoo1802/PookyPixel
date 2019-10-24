package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawBridgeCommandRequest;
import com.kyoo.pixel.connection.components.impl.Bridge;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawBridgeCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final DrawBridgeCommandRequest request;

  public DrawBridgeCommand(ConnectionModel model, DrawBridgeCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    Bridge bridge = new Bridge(request.getId(), request.getStartComponent(),
        request.getEndComponent());
    request.getStartComponent().setStartBridge(Optional.of(bridge));
    request.getEndComponent().setEndBridge(Optional.of(bridge));
    model.addComponent(bridge);
    model.addSelectedComponent(bridge);
    log.debug("Draw Connection Port triggered %s", request);
    return true;
  }

  @Override
  public void undo() {
    request.getStartComponent().setStartBridge(null);
    request.getEndComponent().setEndBridge(null);
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Draw Delete Connection Port triggered %s-%s", request.getCommandType(),
        request.getId());
  }
}