package com.kyoo.pixel.utils;

import com.kyoo.pixel.utils.draw.DrawComponentUtils;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Log4j2
public final class ImageResourceLoader {

  private static Map<ImageKey, BufferedImage> images = new HashMap<>();

  public static void load(FixtureProperties properties) {
    Stream.of(ImageKey.values())
        .forEach(
            img -> {
              BufferedImage bi = loadImage(img);
              if (bi != null) {
                images.put(img, bi);
              }
            });

    DrawComponentUtils.loadBackground(new Dimension(800, 800), properties);
  }

  private static BufferedImage loadImage(ImageKey imgKey) {
    try {
      URL url = ClassLoader.getSystemClassLoader().getResource(imgKey.path);
      if (url != null) {
        return ImageIO.read(url);
      }
    } catch (IOException e) {
      log.error(e);
    }
    return null;
  }

  public enum ImageKey {
    // Connection images
    DRIVER_PORT("connection/driver_port.png");

    private String path;
    private String BASE_PATH = "assets/images/";

    ImageKey(String path) {
      this.path = BASE_PATH + path;
    }
  }
}
