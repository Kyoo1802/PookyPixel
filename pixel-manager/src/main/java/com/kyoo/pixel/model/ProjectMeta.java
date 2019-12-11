package com.kyoo.pixel.model;

import com.google.common.base.Splitter;
import com.kyoo.pixel.utils.io.PixelandiaPaths;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class ProjectMeta {

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

  public boolean pathExists() {
    return Paths.get(path).toFile().exists();
  }

  public Path getFilePath() {
    return Paths.get(path).resolve(name + PixelandiaPaths.EXTENSION);
  }
}
