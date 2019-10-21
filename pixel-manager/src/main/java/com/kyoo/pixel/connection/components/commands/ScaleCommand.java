package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ScalableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.ScaleCommandRequest;
import java.awt.Dimension;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ScaleCommand implements ConnectionCommand {

  private ConnectionModel model;
  private ScaleCommandRequest request;

  public ScaleCommand(ConnectionModel model, ScaleCommandRequest request) {
    this.model = model;
    this.request = request;
  }

  @Override
  public boolean execute() {
    return doScale(false);
  }

  @Override
  public void undo() {
    doScale(true);
  }

  public boolean doScale(boolean isInverse) {
    Optional<SelectableComponent> component =
        model.getCreatedComponentsManager().getComponent(request.getTypeToScale(),
            request.getIdToScale());
    if (component.isEmpty() || !(component.get() instanceof ScalableComponent)) {
      return false;
    }
    ScalableComponent scalableComponent = (ScalableComponent) component.get();
    Dimension scale =
        new Dimension(request.getEndIdxPosition().x - request.getStartIdxPosition().x,
            request.getEndIdxPosition().y - request.getStartIdxPosition().y);
    scale = isInverse ? invert(scale) : scale;
    if (isValidScale(scalableComponent, scale)) {
      scalableComponent.addDimension(scale);
    } else {
      log.debug("Invalid  scale %s", scale);
    }

    log.debug("No component selected to scale %s", request);
    return false;
  }

  private boolean isValidScale(ScalableComponent component, Dimension scale) {
    Dimension d = component.getSize();
    return d.width + scale.width > 0 && d.height + scale.height > 0;
  }

  private Dimension invert(Dimension scale) {
    scale.setSize(-scale.width, -scale.height);
    return scale;
  }
}
