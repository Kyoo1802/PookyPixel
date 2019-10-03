package com.kyoo.pixel.visualizer.components.capturer;

import java.util.concurrent.TimeUnit;
import lombok.Data;

@Data
public final class FrameStats {

  private long lastTime;
  private long delta;
  private long lastFrameCount;
  private int frameRate;
  private int frameNumber;

  public void captureStats() {
    frameNumber++;
    if (lastTime == 0) {
      lastTime = System.nanoTime();
    } else {
      long current = System.nanoTime();
      delta += current - lastTime;
      lastTime = current;
      if (delta > TimeUnit.SECONDS.toNanos(1)) {
        int divisor = (int) (delta / TimeUnit.SECONDS.toNanos(1));
        delta %= TimeUnit.SECONDS.toNanos(1);
        frameRate = (int) ((frameNumber - lastFrameCount) / divisor);
        lastFrameCount = frameNumber;
      }
    }
  }

}
