package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.components.ComponentType;
import java.awt.Point;
import lombok.Builder;
import lombok.Getter;

public abstract class ConnectionCommandRequest {

  public abstract ComponentType getComponentType();

  @Getter
  @Builder(toBuilder = true)
  public static class DrawPanelCommandRequest extends ConnectionCommandRequest {

    private long id;
    private Point startIdxPosition;
    private Point endIdxPosition;
    private ComponentType componentType;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class SelectCommandRequest extends ConnectionCommandRequest {

    private Point idxPosition;
    private ComponentType componentType;
  }
}