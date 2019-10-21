package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.ConnectionComponent.ComponentSide;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.LedComponent;
import java.awt.Point;
import java.util.LinkedHashSet;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public abstract class ConnectionCommandRequest {

  public abstract ComponentType getCommandType();

  @Getter
  @Builder(toBuilder = true)
  public static class DrawDriverPortRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point idxPosition;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DrawSquarePanelCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point startIdxPosition;
    private Point endIdxPosition;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DrawLedPathCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private LinkedHashSet<Led> idxPositions;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class SelectCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point selectIdxPosition;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DrawBridgeCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private ConnectionComponent startComponent;
    private ConnectionComponent endComponent;
  }


  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class MovementCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private long idToMove;
    private ComponentType typeToMove;
    private Point startIdxPosition;
    private Point endIdxPosition;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class ScaleCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private long idToScale;
    private ComponentType typeToScale;
    private ComponentSide cornerToScale;
    private Point startIdxPosition;
    private Point endIdxPosition;
    private int scaleSize;
  }
}
