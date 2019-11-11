package com.kyoo.pixel;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.utils.DrawComponentUtils;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ImageAssets {

  private static Map<ImageKey, BufferedImage> images = new HashMap<>();

  public static void load(FixtureProperties properties) {
    Stream.of(ImageKey.values()).forEach(img -> {
      BufferedImage bi = loadImage(img);
      if (bi != null) {
        images.put(img, bi);
      }
    });

    DrawComponentUtils.loadBackground(new Dimension(800, 800), properties);
  }

  public static BufferedImage getImage(ImageKey image) {
    return images.get(image);
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
