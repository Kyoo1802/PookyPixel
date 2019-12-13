package com.kyoo.pixel.paint;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;

@Getter
@Builder
public class SlideConfiguration {
  @Builder.Default private Dimension initialDimension = new Dimension(800, 600);

  public static SlideConfiguration defaultValues() {
    return SlideConfiguration.builder().build();
  }
}
