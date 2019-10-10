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
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class ConnectionViewModel {

  private BooleanProperty createPanelSelected =
      new SimpleBooleanProperty(false);
  private IntegerProperty canvasWidth =
      new SimpleIntegerProperty(0);
  private IntegerProperty canvasHeight =
      new SimpleIntegerProperty(0);
  private ObjectProperty<Point> position = new SimpleObjectProperty<>(new Point(0, 0));

  private ConnectionModel model;
  private ConnectionActionManager actionManager;

  @Inject
  public ConnectionViewModel(ConnectionModel model, ConnectionActionManager actionManager) {
    this.model = model;
    this.actionManager = actionManager;
    this.createPanelSelected.addListener((observable, oldValue, newValue) ->
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

  public BooleanProperty createPanelSelectedProperty() {
    return createPanelSelected;
  }

  public ObjectProperty<Point> positionProperty() {
    return position;
  }

  public void updateCursorPosition(Point actionPosition) {
    model.handleMove(actionPosition);
  }

  public void handleComponentConnection(Point actionPosition) {
    switch (model.getConnectionAction()) {
      case NO_ACTION:
        handleSelectAction(actionPosition);
        break;
      case DELETE:
        handleDeleteAction(actionPosition);
        break;
      case DRAW:
        handleDrawAction(actionPosition);
      default:
        log.error("Invalid action to handle");
    }
  }

  private void handleSelectAction(Point actionPosition) {
  }

  private void handleDeleteAction(Point actionPosition) {
  }

  private void handleDrawAction(Point actionPosition) {
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

  public IntegerProperty canvasWidthProperty() {
    return canvasWidth;
  }

  public IntegerProperty canvasHeightProperty() {
    return canvasHeight;
  }

  public ConnectionModel getConnectionModel() {
    return model;
  }
}
