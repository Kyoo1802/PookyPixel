package com.kyoo.pixel.data.connection.actions;

import com.kyoo.pixel.data.connection.ComponentType;
import java.awt.Point;
import lombok.Builder;
import lombok.Getter;

public abstract class ConnectionActionRequest {

  public abstract ComponentType getComponentType();

  @Getter
  @Builder(toBuilder = true)
  public static class DrawPanelActionRequest extends ConnectionActionRequest {

    private long id;
    private Point startIdxPosition;
    private Point endIdxPosition;
    private ComponentType componentType;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class SelectActionRequest extends ConnectionActionRequest {

    private Point idxPosition;
    private ComponentType componentType;
  }
}
