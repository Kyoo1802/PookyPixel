package com.kyoo.pixel.paint;

import com.google.common.base.Preconditions;
import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.paint.model.ComponentModel;
import com.kyoo.pixel.paint.model.SlideState;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

public final class SlideManager {
  private Pane rootPane;
  private Map<LayerType, CanvasLayer> canvasLayers;
  private SlideConfiguration slideConfiguration;
  private KeyboardHandler keyboardHandler;
  private SlideServices slideServices;

  private SlideManager() {}

  public static SlideManager of(
      @Nonnull SlideConfiguration slideConfiguration,
      Pane pane,
      KeyboardHandler keyboardHandler,
      SlideServices slideServices) {
    Preconditions.checkNotNull(slideConfiguration);
    Preconditions.checkNotNull(slideServices);
    SlideManager slideManager = new SlideManager();
    slideManager.slideConfiguration = slideConfiguration;
    slideManager.keyboardHandler = keyboardHandler;
    slideManager.slideServices = slideServices;
    slideManager.rootPane = pane;
    slideManager.initialize();
    return slideManager;
  }

  private void initialize() {
    createCanvasLayers();
    setupEvents();
  }

  private void createCanvasLayers() {
    canvasLayers =
        Stream.of(LayerType.values())
            .sorted(Comparator.comparingInt(LayerType::getWeight))
            .map(
                type ->
                    CanvasLayer.builder()
                        .layerType(type)
                        .size(slideConfiguration.getInitialDimension())
                        .build())
            .collect(toUnmodifiableMap(v -> v.getLayerType(), v -> v));
    canvasLayers.entrySet().forEach(e -> rootPane.getChildren().add(e.getValue().getCanvas()));
  }

  private void setupEvents() {
    rootPane.onMouseClickedProperty().set(e -> onMouseClicked(e));
    rootPane.onMouseReleasedProperty().set(e -> onMouseReleased(e));
    rootPane.onMousePressedProperty().set(e -> onMousePressed(e));
    rootPane.onMouseDraggedProperty().set(e -> onMouseDragged(e));
    rootPane.onMouseMovedProperty().set(e -> onMouseMoved(e));
    keyboardHandler.attachListener(e -> onKeyReleased(e));
  }

  private void onMouseMoved(MouseEvent e) {
    if (slideServices.getStateService().currentState() != SlideState.CREATION) {
      Optional<ComponentModel> componentModel =
          slideServices.getComponentService().getFirstComponent(e.getX(), e.getY());
      if (componentModel.isPresent()) {
        slideServices.getStateService().setMoveComponent(componentModel.get());
      } else {
        slideServices.getStateService().updateState(SlideState.SELECTION);
      }
    }
  }

  private void onMousePressed(MouseEvent e) {
    switch (slideServices.getStateService().currentState()) {
      case SELECTION:
        slideServices.getInteractionService().createSelectionRect(e.getX(), e.getY());
        break;
      case MOVE:
        ComponentModel hoveredComponent = slideServices.getStateService().getMoveComponent();
        slideServices.getSelectionService().select(hoveredComponent);
        slideServices.getInteractionService().createMoveRect(hoveredComponent);
        break;
      case CREATION:
        slideServices
            .getInteractiveComponentService()
            .createComponent(e.getX(), e.getY(), slideServices.getStateService().creationRequest());
        break;
    }
  }

  private void onMouseDragged(MouseEvent e) {
    switch (slideServices.getInteractionService().interactionState()) {
      case SELECTION:
        slideServices.getInteractionService().updateSelection(e.getX(), e.getY());
        break;
      case MOVE:
        slideServices.getInteractionService().updateMove(e.getX(), e.getY());
        break;
      case CREATION:
        slideServices.getInteractiveComponentService().updateCreation(e.getX(), e.getY());
        break;
    }
  }

  private void onMouseReleased(MouseEvent e) {
    switch (slideServices.getInteractionService().interactionState()) {
      case SELECTION:
        slideServices.getInteractionService().updateSelection(e.getX(), e.getY());
        Collection<ComponentModel> selectedComponents =
            slideServices
                .getComponentService()
                .getComponents(slideServices.getInteractionService().getSelection());
        slideServices.getSelectionService().selectAll(selectedComponents);
        break;
      case MOVE:
        slideServices.getInteractionService().updateMove(e.getX(), e.getY());
        Collection<ComponentModel> components =
            slideServices.getSelectionService().getSelectedComponents();
        slideServices.getComponentService().moveComponents(components);
        break;
      case CREATION:
        Optional<ComponentModel> createdComponent =
            slideServices.getInteractiveComponentService().getCreatedComponent();
        if (createdComponent.isPresent()) {
          slideServices.getComponentService().addComponent(createdComponent.get());
        }
    }
  }

  private void onKeyReleased(KeyEvent e) {
    System.out.println("onKeyReleased");
  }

  private void onMouseClicked(MouseEvent e) {
    System.out.println("onMouseClicked");
  }
}
