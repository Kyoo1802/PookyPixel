package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawPanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawPanelCommand;
import java.awt.Point;
import java.util.Optional;
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

  @Inject
  public ConnectionViewModel(ConnectionModel model, ConnectionCommandManager actionManager) {
    this.model = model;
    this.actionManager = actionManager;
    this.createSquarePanelSelected
        .addListener((observable, oldValue, newValue) -> model.selectDrawSquare(newValue));
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
        handleDrawAction();
      default:
        log.error("Invalid action to handle");
    }
  }

  private void handleSelectAction() {
  }

  private void handleDeleteAction() {
  }

  private void handleDrawAction() {
    Point actionPosition = mousePosition.get();
    if (model.getBeingCreatedComponent().isEmpty()) {
      switch (model.getDrawAction()) {
        case DRAW_SQUARE_PANEL:
          DrawPanelCommandRequest request =
              DrawPanelCommandRequest.builder()
                  .id(model.generateId(ComponentType.SQUARE_PANEL))
                  .componentType(ComponentType.SQUARE_PANEL)
                  .startIdxPosition(actionPosition).build();
          model.setBeingCreatedComponent(Optional.of(request));
          break;
        default:
          log.error("Invalid Draw Action");
      }
    } else {
      executeDrawingCommand(actionPosition);
    }

  }

  private void executeDrawingCommand(Point actionPosition) {
    switch (model.getDrawAction()) {
      case DRAW_SQUARE_PANEL:
        DrawPanelCommandRequest request =
            ((DrawPanelCommandRequest) model.getBeingCreatedComponent().get())
                .toBuilder()
                .endIdxPosition(actionPosition)
                .build();
        DrawPanelCommand panelAction = new DrawPanelCommand(model, request);
        actionManager.execute(panelAction);
        break;
      default:
        log.error("Invalid Draw Action");
    }
  }

  public ConnectionModel getConnectionModel() {
    return model;
  }
}
