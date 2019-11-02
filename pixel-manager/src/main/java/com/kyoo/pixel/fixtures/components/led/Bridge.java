package com.kyoo.pixel.fixtures.components.led;

import com.kyoo.pixel.fixtures.components.BasicComponentUnit;
import com.kyoo.pixel.fixtures.components.ComponentKey;
import com.kyoo.pixel.fixtures.components.ComponentType;
import java.awt.Dimension;
import lombok.Getter;

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
