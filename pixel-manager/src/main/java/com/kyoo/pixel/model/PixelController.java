package com.kyoo.pixel.model;

import lombok.Data;

@Data
public final class PixelController {

  private int serialSlicedSize;
  private int channelSize;
  private int ledStripLength;

  public int ledTotalCapacity() {
    return channelSize * ledStripLength;
  }
}
