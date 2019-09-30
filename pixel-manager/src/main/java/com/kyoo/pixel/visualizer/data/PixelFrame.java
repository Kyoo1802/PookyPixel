package com.kyoo.pixel.visualizer.data;

import lombok.Data;

@Data
public class PixelFrame {

  private PixelPoint[][] data;

  public int height(){
    return data.length;
  }

  public int width(){
    return data == null || data[0].length==0 ? 0: data[0].length;
  }

  @Data
  final class PixelPoint {
    private int rgba;
  }
}
