package com.kyoo.pixel.visualizer.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import lombok.Data;

@Data
public final class RgbLedStrips {

  private Map<Integer, LinkedList<RgbLed>> stripsByChannelId;

  public RgbLedStrips() {
    stripsByChannelId = new HashMap<>();
  }

  public void addLedRgb(int channelIdx, int rgb) {
    if (!stripsByChannelId.containsKey(channelIdx)) {
      stripsByChannelId.put(channelIdx, new LinkedList<>());
    }
    stripsByChannelId.get(channelIdx).addLast(new RgbLed(rgb));
  }

  @Data
  public final class RgbLed {

    private int rgb;

    public RgbLed(int rgb) {
      this.rgb = rgb;
    }
  }
}
