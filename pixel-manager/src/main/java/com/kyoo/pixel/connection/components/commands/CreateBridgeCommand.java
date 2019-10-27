package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.impl.Bridge;
import com.kyoo.pixel.utils.DrawCommandUtils;
import java.awt.Point;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class CreateBridgeCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final CreateBridgeCommandRequest request;

  public CreateBridgeCommand(ConnectionModel model, CreateBridgeCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    Bridge bridge = new Bridge(request.getId(), request.getStartComponent(),
        request.getEndComponent());
    request.getStartComponent().setNextComponent(Optional.of(request.getEndComponent()));
    model.addComponent(bridge);
    model.addSelectedComponents(bridge);
    log.debug("Draw Connection Port triggered %s", request);
    return true;
  }

  @Override
  public void undo() {
    request.getStartComponent().setNextComponent(Optional.empty());
    request.getEndComponent().setNextComponent(Optional.empty());
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Draw Delete Connection Port triggered %s-%s", request.getCommandType(),
        request.getId());
  }

  @Getter
  @Builder(toBuilder = true)
  public static class CreateBridgeCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private ConnectionComponent startComponent;
    private ConnectionComponent endComponent;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawCommandUtils.drawBridgeCommand(gc, properties, this, pointer);
    }
  }
}
