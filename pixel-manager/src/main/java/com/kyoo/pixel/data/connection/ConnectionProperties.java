package com.kyoo.pixel.data.connection;

import com.google.inject.Singleton;
import lombok.Builder;
import lombok.Getter;

@Builder
@Singleton
@Getter
public final class ConnectionProperties {
  private String backgroundColor;
  private String backgroundDotsColor;
  private String backgroundDotsSize;
  private String squareSize;
  private String ledSize;
  private String ledStartColor;
  private String ledOffColor;
  private String ledEndColor;
  private String selectColor;
  private String noActionColor;
}
