package com.kyoo.pixel.views.stage.fixture.components.led;

import com.kyoo.pixel.views.common.paint.SelectableObject.SelectedSide;
import com.kyoo.pixel.views.stage.fixture.components.BasicComponent;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public final class Led extends BasicComponent {

  private static final Dimension DEFAULT_DIMENSION = new Dimension(1, 1);
  @Getter private final ComponentType componentType;
  @Getter @Setter private SelectedSide selectedSide;

  public Led(long id, Point position) {
    this(id, position, DEFAULT_DIMENSION);
  }

  public Led(long id, Point position, Dimension dimension) {
    super(id, ComponentType.LED);
    this.position = position;
    this.selectedSide = SelectedSide.NONE;
    this.componentType = ComponentType.LED;
    this.size = dimension;
  }
}
