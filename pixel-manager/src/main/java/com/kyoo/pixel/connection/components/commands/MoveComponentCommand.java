package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.utils.DrawCommandUtils;
import java.awt.Point;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class MoveComponentCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final MoveCommandRequest request;

  public MoveComponentCommand(ConnectionModel model, MoveCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    return doMove(false);
  }

  @Override
  public void undo() {
    doMove(true);
  }

  private boolean doMove(boolean isInverse) {
    List<SelectableComponent> components =
        model.getCreatedComponentsManager().getComponents(request.getIdsToMove());

    if (!components.isEmpty()) {
      Point movement = new Point(request.getEndIdxPosition().x - request.getStartIdxPosition().x,
          request.getEndIdxPosition().y - request.getStartIdxPosition().y);
      movement = isInverse ? invert(movement) : movement;
      move(components, movement);
      return true;
    }
    log.debug("No component selected for move %s", request);
    return false;
  }

  private Point invert(Point movement) {
    movement.x = -movement.x;
    movement.y = -movement.y;
    return movement;
  }

  private void move(List<SelectableComponent> components, Point movement) {
    for (SelectableComponent component : components) {
      component.move(movement);
    }
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class MoveCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private List<Long> idsToMove;
    private Point startIdxPosition;
    private Point endIdxPosition;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawCommandUtils.drawMoveCommand(gc, properties, this, pointer);
    }
  }
}
