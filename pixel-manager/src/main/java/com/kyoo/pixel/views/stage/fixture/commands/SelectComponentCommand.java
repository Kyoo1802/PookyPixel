package com.kyoo.pixel.views.stage.fixture.commands;

import com.kyoo.pixel.views.common.paint.SelectableObject.SelectedSide;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveComponentUnit;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.Optional;

@Log4j2
public final class SelectComponentCommand implements FixtureCommand {

  private final FixtureModel model;
  private final SelectCommandRequest request;

  public SelectComponentCommand(FixtureModel model, SelectCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    for (InteractiveComponentUnit iComponent :
        model.getInteractiveComponentManager().getInteractiveComponents()) {
      SelectedSide selectionSide = iComponent.canSelect(request.getSelectIdxPosition());
      if (selectionSide != SelectedSide.NONE) {
        if (request.isMultiSelection()) {
          model.multiSelectComponent(iComponent.getKey());
          log.debug("Multi selection triggered.");
        } else {
          model.singleSelectComponent(iComponent.getKey());
          log.debug("Single selection triggered.");
        }
        return false;
      }
    }
    if (!request.isMultiSelection()) {
      model.clearSelectedComponents();
    }
    log.debug("No selection triggered.");
    model.clearSelectedComponents();
    model.setActiveCommandRequest(Optional.empty());
    return false;
  }

  @Override
  public void undo() {
    // No undo for selection command.
  }

  @Getter
  @Builder(toBuilder = true)
  public static class SelectCommandRequest extends FixtureCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point selectIdxPosition;
    private boolean multiSelection;

    @Override
    public void draw(GraphicsContext gc, FixtureProperties properties, Point pointer) {}
  }
}
