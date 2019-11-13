package com.kyoo.pixel.utils.files;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.kyoo.pixel.utils.CollectionsUtil;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ProjectMetaUtils {

  public static List<ProjectMeta> listProjectMetas(boolean orderedByLastOpened) {
    Optional<Path> appDirPath = PixelandiaPaths.getAppDirPath();

    // There is no home app path
    if (appDirPath.isEmpty()) {
      return Lists.newArrayList();
    }

    // Just create the app directory
    if (!appDirPath.get().toFile().exists()) {
      PixelandiaPaths.createDirPath(appDirPath.get());
      return Lists.newArrayList();
    }

    // Read the first paths file and retrieve all ProjectMetas
    try (Stream<Path> walk = Files.list(appDirPath.get())) {
      Optional<Path> pathsPath =
          walk
              .filter(p -> p.getFileName().endsWith(PixelandiaPaths.pathsFileName()))
              .filter(p -> p.toFile().exists())
              .findFirst();
      if (pathsPath.isPresent()) {
        Stream<ProjectMeta> projects = Files
            .readAllLines(pathsPath.get())
            .stream()
            .map(l -> ProjectMeta.decode(l));
        if (orderedByLastOpened) {
          return projects.collect(CollectionsUtil.inReverse());
        } else {
          return projects.collect(Collectors.toList());
        }
      }
    } catch (IOException e) {
      log.error(e);
    }
    return Lists.newArrayList();
  }

  public static void appendProject(ProjectMeta projectMeta) {
    Optional<Path> appPath = PixelandiaPaths.getAppDirPath();
    // There is no home app path
    if (appPath.isEmpty()) {
      return;
    }

    // Verify path exists
    Path pathFile = appPath.get().resolve(PixelandiaPaths.pathsFileName());
    if (!pathFile.toFile().exists()) {
      PixelandiaPaths.createDirPath(pathFile.getParent());
    }

    // Add projectMeta info to paths file
    File file = pathFile.toFile();
    try {
      FileWriter fileWriter = file.exists()
          ? new FileWriter(file, /* append */ true)
          : new FileWriter(file);
      fileWriter.write(projectMeta.encode() + "\n");
      fileWriter.close();
    } catch (IOException e) {
      log.error(e);
      return;
    }
  }

  public static void removeProject(ProjectMeta project) {
    List<ProjectMeta> newListedProjects = listProjectMetas(false)
        .stream()
        .filter(listedProject -> !listedProject.equals(project))
        .collect(Collectors.toList());
    if (newListedProjects.isEmpty()) {
      return;
    }

    Optional<Path> appPath = PixelandiaPaths.getAppDirPath();
    if (appPath.isEmpty()) {
      return;
    }

    // Add projectMeta info to paths file
    File file = appPath.get().resolve(PixelandiaPaths.pathsFileName()).toFile();
    try {
      FileWriter fileWriter = new FileWriter(file);
      newListedProjects
          .stream()
          .forEach(projectMeta -> {
            try {
              fileWriter.write(projectMeta.encode() + "\n");
            } catch (IOException e) {
              log.error(e);
            }
          });
      fileWriter.close();
    } catch (IOException e) {
      log.error(e);
      return;
    }
  }

  public static void openProject(ProjectMeta projectMeta) {
    removeProject(projectMeta);
    appendProject(projectMeta);

  }

  @Getter
  @Builder
  @ToString
  @EqualsAndHashCode
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

    public boolean exists() {
      return Paths.get(path).toFile().exists();
    }

    public Path getFilePath() {
      return Paths.get(path).resolve(name + PixelandiaPaths.EXTENSION);
    }
  }
}
