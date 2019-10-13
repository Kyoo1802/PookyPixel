package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.MoveCommand;
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
    if (model.thereIsNotComponentBeingCreated()) {
      MovementCommandRequest request =
          MovementCommandRequest.builder()
              .id(model.generateId(ComponentType.MOVEMENT))
              .commandType(ComponentType.MOVEMENT)
              .idToMove(model.getSelectedComponent().get().getId())
              .typeToMove(model.getSelectedComponent().get().getComponentType())
              .startIdxPosition(model.getIdxPointer().getPosition())
              .build();
      model.setBeingCreatedComponent(Optional.of(request));
    } else {
      MovementCommandRequest request =
          ((MovementCommandRequest) model.getBeingCreatedComponent().get())
              .toBuilder()
              .endIdxPosition(model.getIdxPointer().getPosition())
              .build();
      viewModel.executeCommand(new MoveCommand(model, request));
      model.setBeingCreatedComponent(Optional.empty());
    }
  }

  private void handleScale() {
    if (model.thereIsNotComponentBeingCreated()) {
      MovementCommandRequest request =
          MovementCommandRequest.builder()
              .id(model.generateId(ComponentType.MOVEMENT))
              .commandType(ComponentType.MOVEMENT)
              .idToMove(model.getSelectedComponent().get().getId())
              .typeToMove(model.getSelectedComponent().get().getComponentType())
              .startIdxPosition(model.getIdxPointer().getPosition())
              .build();
      model.setBeingCreatedComponent(Optional.of(request));
    } else {
      MovementCommandRequest request =
          ((MovementCommandRequest) model.getBeingCreatedComponent().get())
              .toBuilder()
              .endIdxPosition(model.getIdxPointer().getPosition())
              .build();
      viewModel.executeCommand(new MoveCommand(model, request));
      model.setBeingCreatedComponent(Optional.empty());
    }
  }
}
