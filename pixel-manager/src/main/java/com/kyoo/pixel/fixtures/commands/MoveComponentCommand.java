package com.kyoo.pixel.fixtures.commands;

import com.kyoo.pixel.fixtures.FixtureModel;
import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.components.ComponentKey;
import com.kyoo.pixel.fixtures.components.ComponentType;
import com.kyoo.pixel.utils.DrawCommandUtils;
import java.awt.Point;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class MoveComponentCommand implements FixtureCommand {

  private final FixtureModel model;
  private final MoveCommandRequest request;
  private Point movement;

  public MoveComponentCommand(FixtureModel model, MoveCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    if (movement == null) {
      this.movement = new Point(request.getEndIdxPosition().x - request.getStartIdxPosition().x,
          request.getEndIdxPosition().y - request.getStartIdxPosition().y);
    }
    model.moveComponents(request.keysToMove, movement);
    return true;
  }

  @Override
  public void undo() {
    model.clearSelectedComponents();
    model.moveComponents(request.keysToMove, invert(movement));
  }

  private Point invert(Point movement) {
    movement.x = -movement.x;
    movement.y = -movement.y;
    return movement;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class MoveCommandRequest extends FixtureCommandRequest {

    private long id;
    private ComponentType commandType;
    private List<ComponentKey> keysToMove;
    private Point startIdxPosition;
    private Point endIdxPosition;

    @Override
    public void draw(GraphicsContext gc, FixtureProperties properties, Point pointer) {
      DrawCommandUtils.drawMoveCommand(gc, properties, this, pointer);
    }
  }
}
