package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.util.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ConnectionCanvasRenderer {

  private final ConnectionModel model;
  private final ConnectionProperties properties;

  @Inject
  public ConnectionCanvasRenderer(ConnectionModel model,
      ConnectionProperties properties) {
    this.model = model;
    this.properties = properties;
  }

  public void render(Canvas connectionCanvas) {
    GraphicsContext gc = connectionCanvas.getGraphicsContext2D();
    drawBackground(gc);
    drawCreatedComponents(gc);
    drawCurrentCommand(gc);
    drawMousePointer(gc);
  }

  private void drawBackground(GraphicsContext gc) {
    DrawComponentUtils.getBackground(gc, model.getDimension(), properties);
  }

  private void drawCreatedComponents(GraphicsContext gc) {
    for (Map<Long, SelectableComponent> components :
        model.getCreatedComponentsManager().getComponents().values()) {
      for (SelectableComponent component : components.values()) {
        component.draw(gc, properties);
      }
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
