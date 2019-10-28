package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.impl.SquarePanel;
import com.kyoo.pixel.utils.DrawCommandUtils;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class CreateSquarePanelCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final CreateSquarePanelCommandRequest request;

  public CreateSquarePanelCommand(ConnectionModel model, CreateSquarePanelCommandRequest request) {
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

  @Getter
  @Builder(toBuilder = true)
  public static class CreateSquarePanelCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point startIdxPosition;
    private Point endIdxPosition;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawCommandUtils.drawSquarePanelCommand(gc, properties, this, pointer);
    }
  }
}
