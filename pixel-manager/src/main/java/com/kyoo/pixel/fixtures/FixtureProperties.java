package com.kyoo.pixel.fixtures;

import com.google.inject.Singleton;
import lombok.Builder;
import lombok.Getter;

@Builder
@Singleton
@Getter
public final class FixtureProperties {

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
  private String driverPortColor;
  private String bridgeColor;
  private int selectWidth;
  private int ledConnectionPathWidth;
  private String ledConnectionPathColor;
  private int mouseWidth;
  private String mouseColor;
}
