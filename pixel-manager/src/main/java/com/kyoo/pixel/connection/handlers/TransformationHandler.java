package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.ScaleCommandRequest;
import com.kyoo.pixel.connection.components.commands.MoveCommand;
import com.kyoo.pixel.connection.components.commands.ScaleCommand;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class TransformationHandler {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;

  public TransformationHandler(ConnectionViewModel viewModel) {
    this.viewModel = viewModel;
    this.model = viewModel.getModel();
  }

  public void handleTransformation() {
    if (model.getSelectedComponent().isEmpty()) {
      return;
    }
    switch (model.getTransformationActionState()) {
      case MOVE:
        handleMovement();
        break;
      case SCALE:
        handleScale();
        break;
      default:
        log.error("Invalid transformation: " + model.getTransformationActionState());
    }
  }

  private void handleMovement() {
    if (!model.hasActiveCommandRequest()) {
      MovementCommandRequest request =
          MovementCommandRequest.builder()
              .id(model.generateId(ComponentType.MOVEMENT))
              .commandType(ComponentType.MOVEMENT)
              .idToMove(model.getSelectedComponent().get().getId())
              .typeToMove(model.getSelectedComponent().get().getComponentType())
              .startIdxPosition(model.getPointerCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      MovementCommandRequest request =
          ((MovementCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointerCopy())
              .build();
      viewModel.executeCommand(new MoveCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  private void handleScale() {
    if (!model.hasActiveCommandRequest()) {
      ScaleCommandRequest request =
          ScaleCommandRequest.builder()
              .id(model.generateId(ComponentType.SCALE))
              .commandType(ComponentType.SCALE)
              .idToScale(model.getSelectedComponent().get().getId())
              .typeToScale(model.getSelectedComponent().get().getComponentType())
              .startIdxPosition(model.getPointerCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      ScaleCommandRequest request =
          ((ScaleCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointerCopy())
              .build();
      viewModel.executeCommand(new ScaleCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }
}
