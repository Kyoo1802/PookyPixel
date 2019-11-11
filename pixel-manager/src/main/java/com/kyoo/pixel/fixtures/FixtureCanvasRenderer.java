package com.kyoo.pixel.fixtures;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.kyoo.pixel.fixtures.paint.InteractiveComponentUnit;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.util.concurrent.TimeUnit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class FixtureCanvasRenderer {

  private final FixtureModel model;
  private final FixtureProperties properties;

  @Inject
  public FixtureCanvasRenderer(FixtureModel model,
      FixtureProperties properties) {
    this.model = model;
    this.properties = properties;
  }

  public void render(Canvas connectionCanvas) {
    GraphicsContext gc = connectionCanvas.getGraphicsContext2D();
    Stopwatch sc = Stopwatch.createStarted();
    print("A: ", sc);
    sc.reset();
    sc.start();
    print("B: ", sc);
    sc.reset();
    sc.start();
    drawCurrentCommand(gc);
    print("C: ", sc);
    sc.reset();
    sc.start();
    drawMousePointer(gc);
    print("D: ", sc);
    sc.reset();
    sc.start();
  }

  private void print(String txt, Stopwatch sc) {
    long a2 = sc.elapsed(TimeUnit.MILLISECONDS);
    if (a2 > 1) {
      log.debug(txt + a2);
    }
  }

  public void drawBackground(GraphicsContext gc) {
    DrawComponentUtils.getBackground(gc, model.getDimension(), properties);
  }

  public void drawCreatedComponents(Canvas c) {
    GraphicsContext gc = c.getGraphicsContext2D();
    gc.clearRect(0, 0, c.getWidth(), c.getHeight());
    for (InteractiveComponentUnit component :
        model.getInteractiveComponentManager().getInteractiveComponents()) {
      component.draw(gc, properties);
    }
  }

  private void drawCurrentCommand(GraphicsContext gc) {
    if (model.getActiveCommandRequest().isPresent()) {
      model.getActiveCommandRequest().get()
          .draw(gc, properties, model.getPointer().idxPositionCopy());
    }
  }

  private void drawMousePointer(GraphicsContext gc) {
    model.getPointer().draw(gc, properties);
  }
}
