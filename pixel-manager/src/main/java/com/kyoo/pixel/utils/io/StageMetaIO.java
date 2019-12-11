package com.kyoo.pixel.utils.io;

import com.kyoo.pixel.model.ProjectMeta;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@Log4j2
public final class StageMetaIO {

  private static final String PIXELANDIA_VERSION = "1.0";

  public static void createStageFile(ProjectMeta projectMeta) {
    Path stagePath = projectMeta.getFilePath();
    if (!stagePath.toFile().exists()) {
      PixelandiaPaths.createDirPath(stagePath.getParent());
    }

    // Add projectMeta info to paths file
    File file = stagePath.toFile();
    try {
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(PIXELANDIA_VERSION + "\n");
      fileWriter.close();
    } catch (IOException e) {
      log.error(e);
      return;
    }
  }
}
