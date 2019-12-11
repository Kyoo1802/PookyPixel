package com.kyoo.pixel.inject;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Uses Guice to inject model actionState. Basically you create an instance of GuiceFXMLLoader
 * supplying an Injector, and then call load. The load method takes the FXML file to load, and the
 * controller to create and associate with the FXML file.
 */
@Log4j2
@Singleton
public class GuiceFXMLLoader {

  private final Injector injector;

  @Inject
  public GuiceFXMLLoader(Injector injector) {
    this.injector = injector;
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  public Parent load(URL url, @Nullable Controller parent) {
    return load(url, parent, Maps.newHashMap());
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  public Parent load(URL url, @Nullable Controller parent, Map<String, Object> parameters) {
    return load(url, parent, parameters, Optional.empty());
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  @SuppressWarnings("unchecked")
  public <T> Parent load(
      URL url, @Nullable Controller parent, Map<String, Object> parameters, Optional<T> request) {
    FXMLLoader loader = new FXMLLoader(url, injector.getInstance(ResourceBundle.class));
    parameters
        .entrySet()
        .forEach(entry -> loader.getNamespace().put(entry.getKey(), entry.getValue()));

    loader.setControllerFactory(
        controllerClass -> {
          if (controllerClass == null) {
            return null;
          }
          Controller instance = (Controller) this.injector.getInstance(controllerClass);
          instance.setParent(parent);
          if (request.isPresent() && instance instanceof ControllerRequest) {
            ((ControllerRequest<T>) instance).setRequest(request.get());
          }
          loader.getNamespace().put("controller", instance);
          return instance;
        });
    try {
      return (Parent) loader.load();
    } catch (Exception e) {
      log.error(e);
      e.printStackTrace();
      return null;
    }
  }
}
