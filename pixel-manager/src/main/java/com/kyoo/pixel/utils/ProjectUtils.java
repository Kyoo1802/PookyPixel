package com.kyoo.pixel.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ProjectUtils {

  public static List<ProjectMeta> listProjectMetas() {
    Optional<Path> appDirPath = getAppDirPath();

    // There is no home app path
    if (appDirPath.isEmpty()) {
      return Lists.newArrayList();
    }

    // Just create the app directory
    if (createDirPath(appDirPath.get())) {
      return Lists.newArrayList();
    }

    // Read the first paths file and retrieve all ProjectMetas
    try (Stream<Path> walk = Files.list(appDirPath.get())) {
      Optional<Path> pathsPath =
          walk
              .filter(p -> p.getFileName().endsWith(pathsFileName()))
              .filter(p -> p.toFile().exists())
              .findFirst();
      if (pathsPath.isPresent()) {
        return Files
            .readAllLines(pathsPath.get())
            .stream()
            .map(l -> ProjectMeta.decode(l))
            .collect(Collectors.toList());
      }
    } catch (IOException e) {
      log.error(e);
    }
    return Lists.newArrayList();
  }

  private static boolean createDirPath(Path path) {
    if (!path.toFile().exists()) {
      try {
        Files.createDirectories(path);
        return true;
      } catch (IOException e) {
        log.error(e);
      }
    }
    return false;
  }

  private static String pathsFileName() {
    return "pixelandia.paths";
  }

  private static Optional<Path> getAppDirPath() {
    Path path = null;
    if (OSValidator.isWindows()) {
      log.info("This is Windows");
      path =
          Paths.get(System.getenv("APPDATA") + File.separator + "TargetApp" + File.separator);
    } else if (OSValidator.isMac()) {
      log.info("This is MAC");
      path =
          Paths.get(getHomeDir() + File.separator + "Library" + File.separator
              + "Application Support"
              + File.separator);
    }

    return Optional.ofNullable(path);
  }

  public static String getHomeDir() {
    return System.getProperty("user.home") + File.separator + "Pixelandia";
  }

  public static void createProject(ProjectMeta projectMeta) {
    Optional<Path> appPath = getAppDirPath();

    // There is no home app path
    if (appPath.isEmpty()) {
      return;
    }
    if (createDirPath(appPath.get())) {
      File file = appPath.get().toFile();
      try {
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(projectMeta.encode());
      } catch (IOException e) {
        log.error(e);
        return;
      }
    }

  }


  @Getter
  @Builder
  @ToString
  public static class ProjectMeta {

    private static final String SPLITTER = ";";
    private String name;
    private String path;

    public static ProjectMeta decode(String line) {
      List<String> tokens = Splitter.on(SPLITTER).splitToList(line);

      ProjectMetaBuilder projectMetaBuilder = ProjectMeta.builder();
      if (tokens.isEmpty()) {
        return projectMetaBuilder.build();
      } else if (tokens.size() == 1) {
        return projectMetaBuilder.name(tokens.get(0)).build();
      } else {
        return projectMetaBuilder.name(tokens.get(0)).path(tokens.get(1)).build();
      }
    }

    public String encode() {
      return name + SPLITTER + path;
    }
  }
}
