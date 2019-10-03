package com.kyoo.pixel.visualizer.data;

import com.kyoo.pixel.visualizer.data.RgbLedStrips.RgbLed;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import lombok.Data;

@Data
public final class FrameData {

  private final Map<SlicedMetadata, LinkedList<RgbLed>> slicedData;
  private int frameNumber;

  public FrameData() {
    this.slicedData = new HashMap<>();
  }

  public void add(Integer channelId, int startIdx, RgbLed rgbLed) {
    SlicedMetadata meta = new SlicedMetadata();
    meta.setChannelId(channelId);
    meta.setStartIdx(startIdx);
    if (!slicedData.containsKey(meta)) {
      slicedData.put(meta, new LinkedList<>());
    }
    slicedData.get(meta).add(rgbLed);
  }

  @Data
  public final class SlicedMetadata {

    private int channelId;
    private int startIdx;
  }
}
