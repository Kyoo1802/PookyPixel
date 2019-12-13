package com.kyoo.pixel.paint;

import com.google.common.base.Preconditions;
import javafx.scene.canvas.Canvas;
import lombok.Builder;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.awt.*;

@Getter
public class CanvasLayer<T extends LayerData> {
  private LayerType layerType;
  private Dimension size;
  private Canvas canvas;

  @Builder
  private static CanvasLayer of(LayerType layerType, @Nonnull Dimension size) {
    Preconditions.checkNotNull(size);
    CanvasLayer canvasLayer = new CanvasLayer();
    canvasLayer.layerType = layerType;
    canvasLayer.size = size;
    canvasLayer.canvas = new Canvas(size.width, size.height);
    return canvasLayer;
  }
}
