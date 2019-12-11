package com.kyoo.pixel.views.stage.visualizer.data;

import com.kyoo.pixel.views.stage.visualizer.data.ControllerLedStrips.RgbLed;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Data
public final class SerializedFrame {

  private final Map<SerializedMetadata, LinkedList<RgbLed>> serializedData;
  private int frameNumber;

  public SerializedFrame() {
    this.serializedData = new HashMap<>();
  }

  public void add(Integer channelId, int startIdx, RgbLed rgbLed) {
    SerializedMetadata meta = new SerializedMetadata();
    meta.setChannelId(channelId);
    meta.setStartIdx(startIdx);
    if (!serializedData.containsKey(meta)) {
      serializedData.put(meta, new LinkedList<>());
    }
    serializedData.get(meta).add(rgbLed);
  }

  @Data
  public final class SerializedMetadata {

    private int channelId;
    private int startIdx;
  }
}
