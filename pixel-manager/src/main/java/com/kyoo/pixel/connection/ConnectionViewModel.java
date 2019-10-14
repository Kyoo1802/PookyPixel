package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel.MouseState;
import com.kyoo.pixel.connection.ConnectionModel.TransformationAction;
import com.kyoo.pixel.connection.components.commands.ConnectionCommand;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.handlers.DrawingCommandHandler;
import com.kyoo.pixel.connection.handlers.SelectCommandHandler;
import com.kyoo.pixel.connection.handlers.TransformationHandler;
import java.awt.Point;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
@Getter
public final class ConnectionViewModel {

  private BooleanProperty createDriverPortSelected =
      new SimpleBooleanProperty(false);
  private BooleanProperty createSquarePanelSelected =
      new SimpleBooleanProperty(false);
  private BooleanProperty createLedPathSelected =
      new SimpleBooleanProperty(false);
  private IntegerProperty canvasWidth =
      new SimpleIntegerProperty(0);
  private IntegerProperty canvasHeight =
      new SimpleIntegerProperty(0);
  private ObjectProperty<Point> mousePosition = new SimpleObjectProperty<>(new Point(0, 0));
  private ObjectProperty<MouseState> mouseState = new SimpleObjectProperty<>(MouseState.MOVED);

  private ConnectionModel model;
  private ConnectionCommandManager commandManager;
  private DrawingCommandHandler drawingCommandHandler;
  private SelectCommandHandler selectCommandHandler;
  private TransformationHandler transformationHandler;

  @Inject
  public ConnectionViewModel(ConnectionModel model, ConnectionCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
    this.createSquarePanelSelected
        .addListener(
            (observable, oldValue, newValue) -> model.selectDrawSquarePanelState(newValue));
    this.createDriverPortSelected
        .addListener((observable, oldValue, newValue) -> model.selectDrawDriverPortState(newValue));
    this.createLedPathSelected
        .addListener((observable, oldValue, newValue) -> model.selectDrawLedPathState(newValue));
    this.drawingCommandHandler = new DrawingCommandHandler(this);
    this.selectCommandHandler = new SelectCommandHandler(this);
    this.transformationHandler = new TransformationHandler(this);
  }

  public void handleActions() {
    switch (model.getConnectionActionState()) {
      case NO_ACTION:
        switch (mouseState.get()) {
          case MOVED:
          case DRAGGED:
            model.handlePointerMovement(mousePosition.get());
            break;
          case PRESSED:
            selectCommandHandler.handleSelectAction();
            transformationHandler.handleTransformation();
            break;
          case RELEASED:
            transformationHandler.handleTransformation();
            break;
          case CLICKED:
            selectCommandHandler.handleSelectAction();
            model.setTransformationActionState(TransformationAction.UNSET);
            break;
        }
        break;
      case DRAW_DRIVER_PORT:
      case DRAW_PANEL_BRIDGE:
      case DRAW_SQUARE_PANEL:
      case DRAW_LED_PATH:
        switch (mouseState.get()) {
          case CLICKED:
            drawingCommandHandler.handleDrawAction();
            break;
          case MOVED:
            model.handlePointerMovement(mousePosition.get());
            break;
          default:
        }
        break;
      default:
        log.error("Invalid action to handle: " + model.getConnectionActionState());
    }
  }

  public void executeCommand(ConnectionCommand command) {
    commandManager.execute(command);
  }
}
