package com.kyoo.pixel.views.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.views.connection.ConnectionModel.ConnectionAction;
import com.kyoo.pixel.views.connection.ConnectionModel.DrawAction;
import java.awt.Point;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class ConnectionViewModel {

  private BooleanProperty createPanelSelected =
      new SimpleBooleanProperty(false);
  private IntegerProperty canvasWidth =
      new SimpleIntegerProperty(0);
  private IntegerProperty canvasHeight =
      new SimpleIntegerProperty(0);
  private ObjectProperty<Point> position = new SimpleObjectProperty<>(new Point(0, 0));

  private ConnectionModel connectionModel;

  @Inject
  public ConnectionViewModel(ConnectionModel connectionModel) {
    this.connectionModel = connectionModel;
    this.createPanelSelected.addListener((observable, oldValue, newValue) ->
    {
      if (newValue) {
        connectionModel.selectAction(ConnectionAction.DRAW);
        connectionModel.selectDraw(DrawAction.DRAW_SQUARE_PANEL);
      } else {
        connectionModel.selectAction(ConnectionAction.NO_ACTION);
        connectionModel.selectDraw(DrawAction.UNSET);
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
    connectionModel.handleMove(actionPosition);
  }

  public void handleComponentConnection(Point actionPosition) {
    connectionModel.handleAction(actionPosition);
  }

  public IntegerProperty canvasWidthProperty() {
    return canvasWidth;
  }

  public IntegerProperty canvasHeightProperty() {
    return canvasHeight;
  }

  public ConnectionModel getConnectionModel() {
    return connectionModel;
  }
}
