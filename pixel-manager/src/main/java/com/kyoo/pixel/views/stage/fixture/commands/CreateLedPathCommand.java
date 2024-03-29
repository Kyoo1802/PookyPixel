package com.kyoo.pixel.views.stage.fixture.commands;

import com.kyoo.pixel.utils.draw.DrawCommandUtils;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveLedPath;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.LinkedHashSet;

@Log4j2
public final class CreateLedPathCommand implements FixtureCommand {

  private final FixtureModel model;
  private final CreateLedPathCommandRequest request;
  private InteractiveLedPath iLedPath;

  public CreateLedPathCommand(FixtureModel model, CreateLedPathCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    if (iLedPath == null) {
      iLedPath = InteractiveLedPath.from(request);
    }
    model.addComponent(iLedPath);
    log.debug("Create Led Path {}", request);
    return true;
  }

  @Override
  public void undo() {
    model.removeComponent(iLedPath.getKey());
    log.debug("Delete Led Path {}", request);
  }

  @Getter
  @Builder(toBuilder = true)
  public static class CreateLedPathCommandRequest extends FixtureCommandRequest {

    private long id;
    private ComponentKey componentKey;
    private LinkedHashSet<Point> idxPositions;

    @Override
    public void draw(GraphicsContext gc, FixtureProperties properties, Point pointer) {
      DrawCommandUtils.drawLedPathCommand(gc, properties, this, pointer);
    }
  }
}
