package com.kyoo.pixel.fixtures.commands;

import com.kyoo.pixel.fixtures.FixtureModel;
import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.components.ComponentKey;
import com.kyoo.pixel.fixtures.paint.InteractiveSquarePanel;
import com.kyoo.pixel.utils.DrawCommandUtils;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class CreateSquarePanelCommand implements FixtureCommand {

  private final FixtureModel model;
  private final CreateSquarePanelCommandRequest request;
  private InteractiveSquarePanel iSquarePanel;

  public CreateSquarePanelCommand(FixtureModel model, CreateSquarePanelCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    if (!isValidCommand()) {
      return false;
    }
    if (iSquarePanel == null) {
      iSquarePanel = InteractiveSquarePanel.from(request);
    }
    model.addComponent(iSquarePanel);
    log.debug("Draw Square Panel triggered: {}", request);
    return true;
  }

  private boolean isValidCommand() {
    return request.getEndIdxPosition().x - request.getStartIdxPosition().x + 1 > 0
        && request.getEndIdxPosition().y - request.getStartIdxPosition().y + 1 > 0;
  }

  @Override
  public void undo() {
    model.removeComponent(iSquarePanel.getKey());
    log.debug("Draw Delete Square Panel triggered {}", request);
  }

  @Getter
  @Builder(toBuilder = true)
  public static class CreateSquarePanelCommandRequest extends FixtureCommandRequest {

    private long id;
    private ComponentKey componentKey;
    private Point startIdxPosition;
    private Point endIdxPosition;

    @Override
    public void draw(GraphicsContext gc, FixtureProperties properties, Point pointer) {
      DrawCommandUtils.drawSquarePanelCommand(gc, properties, this, pointer);
    }
  }
}
