package com.kyoo.pixel.views.stage.fixture.commands;

import com.kyoo.pixel.utils.draw.DrawCommandUtils;
import com.kyoo.pixel.views.common.paint.SelectableObject.SelectedSide;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Log4j2
public final class ScaleComponentCommand implements FixtureCommand {

  private final FixtureModel model;
  private final ScaleCommandRequest request;
  private Dimension scale;

  public ScaleComponentCommand(FixtureModel model, ScaleCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    if (scale == null) {
      this.scale =
          new Dimension(
              request.getEndIdxPosition().x - request.getStartIdxPosition().x,
              request.getEndIdxPosition().y - request.getStartIdxPosition().y);
    }
    return model.getInteractiveComponentManager().scale(request.getKeyToScale(), scale);
  }

  @Override
  public void undo() {
    model.getInteractiveComponentManager().scale(request.getKeyToScale(), invert(scale));
  }

  private Dimension invert(Dimension scale) {
    scale.setSize(-scale.width, -scale.height);
    return scale;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class ScaleCommandRequest extends FixtureCommandRequest {

    private long id;
    private ComponentKey keyToScale;
    private SelectedSide sideToScale;
    private Point componentStartIdxPoint;
    private Point startIdxPosition;
    private Point endIdxPosition;
    private int scaleSize;

    @Override
    public void draw(GraphicsContext gc, FixtureProperties properties, Point pointer) {
      DrawCommandUtils.drawScaleCommand(gc, properties, this, pointer);
    }
  }
}
