package com.kyoo.pixel.utils.files;

import com.kyoo.pixel.utils.files.ProjectMetaUtils.ProjectMeta;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class StageMetaUtils {

  private static final String PIXELANDIA_VERSION = "1.0";

  public static void createStage(ProjectMeta projectMeta) {
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
