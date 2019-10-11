package com.kyoo.pixel.views.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.data.connection.ComponentType;
import com.kyoo.pixel.data.connection.actions.ConnectionActionManager;
import com.kyoo.pixel.data.connection.actions.ConnectionActionRequest.DrawPanelActionRequest;
import com.kyoo.pixel.data.connection.actions.DrawPanelAction;
import com.kyoo.pixel.views.connection.ConnectionModel.ConnectionAction;
import com.kyoo.pixel.views.connection.ConnectionModel.DrawAction;
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
  private ConnectionActionManager actionManager;

  @Inject
  public ConnectionViewModel(ConnectionModel model, ConnectionActionManager actionManager) {
    this.model = model;
    this.actionManager = actionManager;
    this.createSquarePanelSelected.addListener((observable, oldValue, newValue) ->
    {
      if (newValue) {
        model.selectAction(ConnectionAction.DRAW);
        model.selectDraw(DrawAction.DRAW_SQUARE_PANEL);
      } else {
        model.selectAction(ConnectionAction.NO_ACTION);
        model.selectDraw(DrawAction.UNSET);
      }
    });
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
          DrawPanelActionRequest request =
              DrawPanelActionRequest.builder().id(1).componentType(ComponentType.SQUARE_PANEL)
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
        DrawPanelActionRequest request =
            ((DrawPanelActionRequest) model.getBeingCreatedComponent().get())
                .toBuilder()
                .endIdxPosition(actionPosition)
                .build();
        DrawPanelAction panelAction = new DrawPanelAction(model, request);
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
