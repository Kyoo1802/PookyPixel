package com.kyoo.pixel.inject;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.kyoo.pixel.utils.ControllerRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.log4j.Log4j2;

/**
 * Uses Guice to inject model actionState. Basically you create an instance of GuiceFXMLLoader
 * supplying an Injector, and then call load. The load method takes the FXML file to load, and the
 * controller to create and associate with the FXML file.
 */
@Log4j2
public class GuiceFXMLLoader {

  private final Injector injector;

  @Inject
  public GuiceFXMLLoader(Injector injector) {
    this.injector = injector;
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  public Parent load(URL url) {
    return load(url, Maps.newHashMap());
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  public Parent load(URL url, Map<String, Object> parameters) {
    return load(url, parameters, Optional.empty());
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  public <T> Parent load(URL url, Map<String, Object> parameters, Optional<T> request) {
    FXMLLoader loader = new FXMLLoader(url, injector.getInstance(ResourceBundle.class));
    parameters.entrySet()
        .forEach(entry -> loader.getNamespace().put(entry.getKey(), entry.getValue()));

    loader.setControllerFactory(controllerClass -> {
      if (controllerClass == null) {
        return null;
      }
      Object instance = this.injector.getInstance(controllerClass);
      if (request.isPresent() && instance instanceof ControllerRequest) {
        ((ControllerRequest<T>) instance).setRequest(request.get());
      }
      loader.getNamespace().put("controller", instance);
      return instance;
    });
    try {
      return (Parent) loader.load();
    } catch (IOException e) {
      log.error(e);
      e.printStackTrace();
      return null;
    }
  }
}
