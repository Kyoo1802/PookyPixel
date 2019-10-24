package com.kyoo.pixel.connection.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.ScaleCommandRequest;
import com.kyoo.pixel.connection.components.commands.MoveCommand;
import com.kyoo.pixel.connection.components.commands.ScaleCommand;
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
    if (model.getSelectedComponent().isEmpty()) {
      return;
    }
    switch (model.getTransformationAction()) {
      case MOVE:
        handleMovement();
        break;
      case SCALE:
        handleScale();
        break;
      default:
        log.error("Invalid transformation: " + model.getTransformationAction());
    }
  }

  private void handleMovement() {
    if (!model.hasActiveCommandRequest()) {
      MovementCommandRequest request =
          MovementCommandRequest.builder()
              .id(model.generateId(ComponentType.MOVEMENT))
              .commandType(ComponentType.MOVEMENT)
              .idsToMove(model.getSelectedComponent().values().stream().map(c -> c.getId()).collect(
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
      commandManager.execute(new MoveCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  private void handleScale() {
    // We don't support more than one element to scale
    if (model.getSelectedComponent().size() != 1) {
      return;
    }
    if (!model.hasActiveCommandRequest()) {
      ScaleCommandRequest request =
          ScaleCommandRequest.builder()
              .id(model.generateId(ComponentType.SCALE))
              .commandType(ComponentType.SCALE)
              .idToScale(
                  model.getSelectedComponent().values().stream().map(c -> c.getId()).findFirst()
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
      commandManager.execute(new ScaleCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }
}
