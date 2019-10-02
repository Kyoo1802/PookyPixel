package com.kyoo.pixel.visualizer;

import lombok.Data;

@Data
public final class PixelController {
  private int sliceSize;
  private int channelSize;
  private int ledStripLength;
  public int ledTotalCapacity(){
    return channelSize * ledStripLength;
  }
}
