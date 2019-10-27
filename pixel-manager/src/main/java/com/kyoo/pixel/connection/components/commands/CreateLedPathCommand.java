package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.impl.Led;
import com.kyoo.pixel.connection.components.impl.LedPath;
import com.kyoo.pixel.utils.DrawCommandUtils;
import java.awt.Point;
import java.util.LinkedHashSet;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class CreateLedPathCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final CreateLedPathCommandRequest request;

  public CreateLedPathCommand(ConnectionModel model, CreateLedPathCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    LedPath ledPath = new LedPath(request.getId(), request.getIdxPositions());
    model.addComponent(ledPath);
    model.addSelectedComponents(ledPath);
    log.debug("Create Led Path [%s, %d]", request.getCommandType(), request.getId());
    return true;
  }

  @Override
  public void undo() {
    model.removeComponent(request.getCommandType(), request.getId());
    log.debug("Delete Led Path [%s, %d]", request.getCommandType(), request.getId());
  }

  @Getter
  @Builder(toBuilder = true)
  public static class CreateLedPathCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private LinkedHashSet<Led> idxPositions;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawCommandUtils.drawLedPathCommand(gc, properties, this, pointer);
    }
  }
}
