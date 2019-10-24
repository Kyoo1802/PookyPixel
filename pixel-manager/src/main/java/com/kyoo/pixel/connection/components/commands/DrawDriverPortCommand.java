package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
import com.kyoo.pixel.connection.components.impl.DriverPort;
import java.awt.Dimension;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawDriverPortCommand implements ConnectionCommand {

  public static final Dimension DEFAULT_CONNECTION_PORT_SIZE = new Dimension(2, 2);

  private final ConnectionModel model;
  private final DrawDriverPortRequest request;

  public DrawDriverPortCommand(ConnectionModel model, DrawDriverPortRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    DriverPort port = new DriverPort(request.getId(), request.getIdxPosition(),
        DEFAULT_CONNECTION_PORT_SIZE);
    model.addComponent(port);
    model.addSelectedComponent(port);
    log.debug("Draw Driver Port triggered %s", request.getIdxPosition());
    return true;
  }

  @Override
  public void undo() {
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Draw Delete Driver Port triggered %s-%s", request.getCommandType(),
        request.getId());
  }
}
