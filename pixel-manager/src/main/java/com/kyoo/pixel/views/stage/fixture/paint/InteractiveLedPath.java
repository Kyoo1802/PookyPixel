package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.utils.draw.DrawComponentUtils;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.commands.CreateLedPathCommand.CreateLedPathCommandRequest;
import com.kyoo.pixel.views.stage.fixture.components.led.Led;
import com.kyoo.pixel.views.stage.fixture.components.led.LedPath;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

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
