package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.MoveCommand;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import java.util.Optional;

public final class TransformationHandler {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;

  public TransformationHandler(ConnectionViewModel viewModel) {
    this.viewModel = viewModel;
    this.model = viewModel.getModel();
  }

  public void handleTransformation() {
    handleMovement();
  }

  private void handleMovement() {
    Point actionPosition = PositionUtils.toIdxPosition(viewModel.getMousePosition().get());
    if (model.getBeingCreatedComponent().isEmpty()) {
      startMovement(actionPosition);
    } else {
      endMovement(actionPosition);
    }
  }

  private void startMovement(Point actionPosition) {
    MovementCommandRequest request =
        MovementCommandRequest.builder()
            .id(model.generateId(ComponentType.MOVEMENT))
            .componentType(ComponentType.UNSPECIFIED)
            .startIdxPosition(actionPosition)
            .build();
    model.setBeingCreatedComponent(Optional.of(request));
  }

  private void endMovement(Point actionPosition) {
    MovementCommandRequest request =
        ((MovementCommandRequest) model.getBeingCreatedComponent().get())
            .toBuilder()
            .endIdxPosition(actionPosition)
            .build();
    viewModel.executeCommand(new MoveCommand(model, request));
    model.setBeingCreatedComponent(Optional.empty());
  }
}
