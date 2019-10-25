package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import com.kyoo.pixel.connection.components.impl.Led;
import com.kyoo.pixel.utils.DrawUtils;
import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public abstract class ConnectionCommandRequest {

  public abstract ComponentType getCommandType();

  public abstract void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer);


  @Getter
  @Builder(toBuilder = true)
  public static class DrawDriverPortRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point idxPosition;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {

    }
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DrawSquarePanelCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point startIdxPosition;
    private Point endIdxPosition;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawUtils.drawSquarePanelCommand(gc, properties, this, pointer);
    }
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DrawLedPathCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private LinkedHashSet<Led> idxPositions;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawUtils.drawLedPathCommand(gc, properties, this, pointer);
    }
  }

  @Getter
  @Builder(toBuilder = true)
  public static class SelectCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point selectIdxPosition;
    private boolean multiSelection;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
    }
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DrawBridgeCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private ConnectionComponent startComponent;
    private ConnectionComponent endComponent;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawUtils
          .drawBridgeCommand(gc, properties, this, pointer);
    }
  }


  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class MovementCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private List<Long> idsToMove;
    private Point startIdxPosition;
    private Point endIdxPosition;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawUtils.drawMovementCommand(gc, properties, this, pointer);
    }
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class ScaleCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Long idToScale;
    private SelectedSide sideToScale;
    private Point componentStartIdxPoint;
    private Point startIdxPosition;
    private Point endIdxPosition;
    private int scaleSize;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
      DrawUtils.drawScaleCommand(gc, properties, this, pointer);
    }
  }
}
