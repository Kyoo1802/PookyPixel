package com.kyoo.pixel.fixtures.paint;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.commands.CreateLedPathCommand.CreateLedPathCommandRequest;
import com.kyoo.pixel.fixtures.components.led.Led;
import com.kyoo.pixel.fixtures.components.led.LedPath;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;

public class InteractiveLedPath extends InteractiveComponent {

  public InteractiveLedPath(LedPath ledPath) {
    super(ledPath);
  }

  public static InteractiveLedPath from(CreateLedPathCommandRequest request) {
    return from(new LedPath(request.getId(), request.getIdxPositions()));
  }

  private static InteractiveLedPath from(LedPath ledPath) {
    return new InteractiveLedPath(ledPath);
  }

  @Override
  public void drawComponent(GraphicsContext gc, FixtureProperties properties) {
    DrawComponentUtils.drawLedComponent(gc, properties, getComponent());
  }

  @Override
  public SelectedSide canSelect(Point xy) {
    for (Led led : getComponent().getLeds().values()) {
      if (led.intersects(xy)) {
        return SelectedSide.CENTER;
      }
    }
    return SelectedSide.NONE;
  }

  @Override
  public LedPath getComponent() {
    return (LedPath) super.getComponent();
  }
}