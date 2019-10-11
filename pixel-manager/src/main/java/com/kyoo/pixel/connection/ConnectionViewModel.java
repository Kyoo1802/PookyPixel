package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.handlers.DrawingCommandHandler;
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

  private BooleanProperty createSquarePanelSelected =
      new SimpleBooleanProperty(false);
  private IntegerProperty canvasWidth =
      new SimpleIntegerProperty(0);
  private IntegerProperty canvasHeight =
      new SimpleIntegerProperty(0);
  private ObjectProperty<Point> mousePosition = new SimpleObjectProperty<>(new Point(0, 0));

  private ConnectionModel model;
  private ConnectionCommandManager actionManager;
  private DrawingCommandHandler drawingCommandHandler;

  @Inject
  public ConnectionViewModel(ConnectionModel model, ConnectionCommandManager actionManager) {
    this.model = model;
    this.actionManager = actionManager;
    this.createSquarePanelSelected
        .addListener((observable, oldValue, newValue) -> model.selectDrawSquare(newValue));
    this.drawingCommandHandler = new DrawingCommandHandler(this);
  }

  public void updateCursorPosition() {
    model.handleMove(mousePosition.get());
  }

  public void handleComponentConnection() {
    switch (model.getConnectionAction()) {
      case NO_ACTION:
        handleSelectAction();
        break;
      case DELETE:
        handleDeleteAction();
        break;
      case DRAW:
        drawingCommandHandler.handleDrawAction();
        break;
      default:
        log.error("Invalid action to handle");
    }
  }

  private void handleSelectAction() {
  }

  private void handleDeleteAction() {
  }
}
