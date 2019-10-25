package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.SelectCommandRequest;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SelectCommand implements ConnectionCommand {

  private final ConnectionModel model;
  private final SelectCommandRequest request;

  public SelectCommand(ConnectionModel model, SelectCommandRequest request) {
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

        if (selectionSide != SelectedSide.NONE) {
          log.debug("Selection triggered.");
          if(component.getSelectedSide() != SelectedSide.NONE){
            log.debug("Skip selection triggered.");
            return false;
          } else if (model.getSelectedComponents().isEmpty() || !request.isMultiSelection()){
            model.singleSelection(component, selectionSide);
            log.debug("Single selection triggered.");
            return true;
          } else {
            model.multiSelection(component, selectionSide);
            log.debug("Multi selection triggered.");
            return true;
          }
        }

      }
    }
    log.debug("No selection triggered.");
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
}
