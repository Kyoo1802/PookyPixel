package com.kyoo.pixel.utils.fx;

import javafx.stage.StageStyle;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class StageProperties {
  private StageStyle style;
  private boolean maximized;
  private boolean resizable;
  private boolean sizeToScene;
  private boolean centerToScreen;

  public static StageProperties defaultValues() {
    return defaultValuesBuilder().build();
  }

  public static StageProperties.StagePropertiesBuilder defaultValuesBuilder() {
    return StageProperties.builder()
        .maximized(true)
        .style(StageStyle.DECORATED)
        .resizable(true)
        .sizeToScene(true)
        .centerToScreen(true);
  }
}
