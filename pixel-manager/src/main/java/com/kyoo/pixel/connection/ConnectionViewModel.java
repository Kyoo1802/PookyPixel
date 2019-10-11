package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
  private IntegerProperty canvasWidth =
      new SimpleIntegerProperty(0);
  private IntegerProperty canvasHeight =
      new SimpleIntegerProperty(0);
  private ObjectProperty<Point> mousePosition = new SimpleObjectProperty<>(new Point(0, 0));

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
    this.drawingCommandHandler = new DrawingCommandHandler(this);
    this.selectCommandHandler = new SelectCommandHandler(this);
    this.transformationHandler = new TransformationHandler(this);
  }

  public void updateCursorPosition() {
    model.handleMove(mousePosition.get());
  }

  public void handleComponentConnection() {
    switch (model.getConnectionActionState()) {
      case NO_ACTION:
        selectCommandHandler.handleSelectAction();
        break;
      case DRAW:
        drawingCommandHandler.handleDrawAction();
        break;
      case TRANSFORMATION:
        transformationHandler.handleTransformation();
        break;
      default:
        log.error("Invalid action to handle: " + model.getConnectionActionState());
    }
  }

  public void executeCommand(ConnectionCommand command) {
    commandManager.execute(command);
  }
}
