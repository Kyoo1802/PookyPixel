package com.kyoo.pixel.connection.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionModel.TransformationAction;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.components.commands.MoveComponentCommand;
import com.kyoo.pixel.connection.components.commands.MoveComponentCommand.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ScaleComponentCommand;
import com.kyoo.pixel.connection.components.commands.ScaleComponentCommand.ScaleCommandRequest;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class TransformationHandler {

  private final ConnectionModel model;
  private final ConnectionCommandManager commandManager;

  @Inject
  public TransformationHandler(ConnectionModel model, ConnectionCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void handleTransformation() {
    if (model.getSelectedComponents().isEmpty()) {
      return;
    }
    model.setTransformationAction(computeTransformation());
    switch (model.getTransformationAction()) {
      case MOVE:
        handleMovement();
        break;
      case SCALE:
        handleScale();
        break;
      case UNSET:
        break;
      default:
        log.error("Invalid transformation: " + model.getTransformationAction());
    }
  }

  private TransformationAction computeTransformation() {
    for (Entry<Long, SelectableComponent> kC : model.getSelectedComponents().entrySet()) {
      if (kC.getValue().getSelectedSide() == SelectedSide.CENTER) {
        return TransformationAction.MOVE;
      } else if (kC.getValue().getSelectedSide() != SelectedSide.NONE
          && model.getSelectedComponents().size() == 1) {
        return TransformationAction.SCALE;
      }
    }
    return TransformationAction.UNSET;
  }

  private void handleMovement() {
    if (!model.hasActiveCommandRequest()) {
      MovementCommandRequest request =
          MovementCommandRequest.builder()
              .id(model.generateId(ComponentType.MOVEMENT))
              .commandType(ComponentType.MOVEMENT)
              .idsToMove(
                  model.getSelectedComponents().values().stream().map(c -> c.getId()).collect(
                      Collectors.toList()))
              .startIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      MovementCommandRequest request =
          ((MovementCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      commandManager.execute(new MoveComponentCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  private void handleScale() {
    // We don't support more than one element to scale
    if (model.getSelectedComponents().size() != 1) {
      return;
    }
    if (!model.hasActiveCommandRequest()) {
      ScaleCommandRequest request =
          ScaleCommandRequest.builder()
              .id(model.generateId(ComponentType.SCALE))
              .commandType(ComponentType.SCALE)
              .idToScale(
                  model.getSelectedComponents().values().stream().map(c -> c.getId()).findFirst()
                      .get())
              .startIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      ScaleCommandRequest request =
          ((ScaleCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      commandManager.execute(new ScaleComponentCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }
}
