package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.components.ComponentType;
import java.awt.Point;
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
  public static class SelectCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point selectIdxPosition;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class MovementCommandRequest extends ConnectionCommandRequest {

    private long id;
    private long idToMove;
    private ComponentType commandType;
    private ComponentType typeToMove;
    private Point startIdxPosition;
    private Point endIdxPosition;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  public static class ScaleCommandRequest extends ConnectionCommandRequest {

    private long id;
    private long idToScale;
    private ComponentType commandType;
    private ComponentType typeToScale;
    private COMPONENT_CORNER cornerToScale;
    private int scaleSize;
  }
  public enum COMPONENT_CORNER {
    UPPER_LEFT,
    UPPER_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
  }
}
