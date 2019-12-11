package com.kyoo.pixel.utils.io;

import com.google.common.collect.Lists;
import com.kyoo.pixel.model.ProjectMeta;
import com.kyoo.pixel.utils.CollectionsUtil;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public final class ProjectMetaIO {

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
          walk.filter(p -> p.getFileName().endsWith(PixelandiaPaths.pathsFileName()))
              .filter(p -> p.toFile().exists())
              .findFirst();
      if (pathsPath.isPresent()) {
        Stream<ProjectMeta> projects =
            Files.readAllLines(pathsPath.get()).stream().map(l -> ProjectMeta.decode(l));
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

  public static void appendProjectMeta(ProjectMeta projectMeta) {
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
      FileWriter fileWriter =
          file.exists() ? new FileWriter(file, /* append */ true) : new FileWriter(file);
      fileWriter.write(projectMeta.encode() + "\n");
      fileWriter.close();
    } catch (IOException e) {
      log.error(e);
      return;
    }
  }

  public static void removeProject(ProjectMeta project) {
    List<ProjectMeta> newListedProjects =
        listProjectMetas(false).stream()
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
      newListedProjects.stream()
          .forEach(
              projectMeta -> {
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
    appendProjectMeta(projectMeta);
  }
}
