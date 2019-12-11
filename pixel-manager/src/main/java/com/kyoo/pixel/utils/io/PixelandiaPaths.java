package com.kyoo.pixel.utils.io;

import com.kyoo.pixel.utils.OSValidator;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Log4j2
public final class PixelandiaPaths {

  public static final String EXTENSION = ".pixelandia";

  public static String pathsFileName() {
    return "pixelandia.paths";
  }

  public static Optional<Path> getAppDirPath() {
    Path path = null;
    if (OSValidator.isWindows()) {
      log.info("This is Windows");
      path =
          Paths.get(
              System.getenv("APPDATA")
                  + File.separator
                  + "TargetApp"
                  + File.separator
                  + "Pixelandia"
                  + File.separator);
    } else if (OSValidator.isMac()) {
      log.info("This is MAC");
      path =
          Paths.get(
              System.getProperty("user.home")
                  + File.separator
                  + "Library"
                  + File.separator
                  + "Application Support"
                  + File.separator
                  + "Pixelandia"
                  + File.separator);
    }

    return Optional.ofNullable(path);
  }

  public static String getHomeDir() {
    return System.getProperty("user.home") + File.separator + "Pixelandia";
  }

  public static boolean createDirPath(Path path) {
    try {
      Files.createDirectories(path);
      return true;
    } catch (IOException e) {
      log.error(e);
    }
    return false;
  }
}
