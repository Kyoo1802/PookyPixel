package com.kyoo.pixel.fixtures.paint;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.commands.CreateBridgeCommand.CreateBridgeCommandRequest;
import com.kyoo.pixel.fixtures.components.led.Bridge;
import com.kyoo.pixel.utils.DrawComponentUtils;
import javafx.scene.canvas.GraphicsContext;

public final class InteractiveBridge extends InteractiveComponentUnit {

  public InteractiveBridge(Bridge bridge) {
    super(bridge);
  }

  public static InteractiveBridge from(CreateBridgeCommandRequest request) {
    return from(new Bridge(request.getId(), request.getStartComponent().getKey(),
        request.getEndComponent().getKey()));
  }

  private static InteractiveBridge from(Bridge bridge) {
    return new InteractiveBridge(bridge);
  }

  @Override
  public void drawComponent(GraphicsContext gc, FixtureProperties properties) {
    DrawComponentUtils.drawBridge(gc, properties, getComponentUnit());
  }

  @Override
  public Bridge getComponentUnit() {
    return (Bridge) super.getComponentUnit();
  }
}
