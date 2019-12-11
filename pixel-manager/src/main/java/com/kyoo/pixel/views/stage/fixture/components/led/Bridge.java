package com.kyoo.pixel.views.stage.fixture.components.led;

import com.kyoo.pixel.views.stage.fixture.components.BasicComponentUnit;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;
import lombok.Getter;

import java.awt.*;

@Getter
public final class Bridge extends BasicComponentUnit {

  private ComponentKey startComponentKey;
  private ComponentKey endComponentKey;

  public Bridge(long id, ComponentKey startComponentKey, ComponentKey endComponentKey) {
    super(id, ComponentType.BRIDGE);
    this.startComponentKey = startComponentKey;
    this.endComponentKey = endComponentKey;
    this.size = new Dimension(2, 2);
  }
}
