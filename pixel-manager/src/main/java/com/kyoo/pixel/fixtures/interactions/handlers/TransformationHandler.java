package com.kyoo.pixel.fixtures.interactions.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.fixtures.FixtureCommandManager;
import com.kyoo.pixel.fixtures.FixtureModel;
import com.kyoo.pixel.fixtures.commands.MoveComponentCommand;
import com.kyoo.pixel.fixtures.commands.MoveComponentCommand.MoveCommandRequest;
import com.kyoo.pixel.fixtures.commands.ScaleComponentCommand;
import com.kyoo.pixel.fixtures.commands.ScaleComponentCommand.ScaleCommandRequest;
import com.kyoo.pixel.fixtures.components.ComponentType;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class TransformationHandler {

  private final FixtureModel model;
  private final FixtureCommandManager commandManager;

  @Inject
  public TransformationHandler(FixtureModel model, FixtureCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void startTransformation() {
    // Transformations are applied to just selected components
    if (model.getInteractiveComponentManager().selectedComponents().isEmpty()) {
      return;
    }
    if (model.canScale(model.getPointer().getCanvasPosition())) {
      ScaleCommandRequest request =
          ScaleCommandRequest.builder()
              .id(model.generateCommandId())
              .keyToScale(model.selectedComponentKeys().get(0))
              .startIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else if (model.canMove(model.getPointer().getIdxPosition())) {
      MoveCommandRequest request =
          MoveCommandRequest.builder()
              .id(model.generateCommandId())
              .commandType(ComponentType.MOVEMENT)
              .keysToMove(model.selectedComponentKeys())
              .startIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    }
  }

  public void endTransformation() {
    if (model.getActiveCommandRequest().isEmpty()) {
      return;
    }
    if (model.getActiveCommandRequest().get() instanceof ScaleCommandRequest) {
      ScaleCommandRequest request =
          ((ScaleCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      commandManager.execute(new ScaleComponentCommand(model, request));
    } else if (model.getActiveCommandRequest().get() instanceof MoveCommandRequest) {
      MoveCommandRequest request =
          ((MoveCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      commandManager.execute(new MoveComponentCommand(model, request));
    }
    model.setActiveCommandRequest(Optional.empty());
  }

  public void cancel() {
    model.setActiveCommandRequest(Optional.empty());
  }
}
