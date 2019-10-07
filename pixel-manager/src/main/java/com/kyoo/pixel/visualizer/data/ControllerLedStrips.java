package com.kyoo.pixel.visualizer.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Data;

@Data
public final class ControllerLedStrips {

  private Map<Integer, LinkedList<RgbLed>> stripsByChannelId;

  public ControllerLedStrips() {
    stripsByChannelId = new HashMap<>();
  }

  public void addLedRgb(int channelIdx, int rgb) {
    if (!stripsByChannelId.containsKey(channelIdx)) {
      stripsByChannelId.put(channelIdx, new LinkedList<>());
    }
    stripsByChannelId.get(channelIdx).addLast(new RgbLed(rgb));
  }

  public int rgbLedCount() {
    int count = 0;
    for (Entry<Integer, LinkedList<RgbLed>> entryStrip :
        stripsByChannelId.entrySet()) {
      count += entryStrip.getValue().size();
    }
    return count;
  }

  @Data
  public final class RgbLed {

    private int rgb;

    public RgbLed(int rgb) {
      this.rgb = rgb;
    }
  }
}
