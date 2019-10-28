package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import java.awt.Point;
import java.util.Map;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SelectComponentCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final SelectCommandRequest request;

  public SelectComponentCommand(ConnectionModel model, SelectCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    for (Map<Long, SelectableComponent> components :
        model.getCreatedComponentsManager().getComponents().values()) {
      for (SelectableComponent component : components.values()) {
        SelectedSide selectionSide = component
            .hasSelection(request.getSelectIdxPosition().x, request.getSelectIdxPosition().y);

        if (selectionSide == SelectedSide.NONE) {
          if (!request.isMultiSelection()) {
            model.unSelect(component);
          }
        } else {
          if (!request.isMultiSelection()) {
            model.clearSelection();
          }
          model.select(component, selectionSide);
          log.debug("Selection triggered.");
          return true;
        }
      }
    }
    log.debug("No selection triggered.");
    model.getSelectedComponents().clear();
    model.setActiveCommandRequest(Optional.empty());
    return false;
  }

  @Override
  public void undo() {
    for (Map<Long, SelectableComponent> components :
        model.getCreatedComponentsManager().getComponents().values()) {
      for (SelectableComponent component : components.values()) {
        SelectedSide selectionSide = component
            .hasSelection(request.getSelectIdxPosition().x, request.getSelectIdxPosition().y);
        if (selectionSide != SelectedSide.NONE) {
          model.unSelect(component);
          return;
        }
      }
    }
  }

  @Getter
  @Builder(toBuilder = true)
  public static class SelectCommandRequest extends ConnectionCommandRequest {

    private long id;
    private ComponentType commandType;
    private Point selectIdxPosition;
    private boolean multiSelection;

    @Override
    public void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer) {
    }
  }
}
