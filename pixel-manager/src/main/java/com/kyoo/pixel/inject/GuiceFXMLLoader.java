package com.kyoo.pixel.inject;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Uses Guice to inject model state. Basically you create an instance of GuiceFXMLLoader supplying
 * an Injector, and then call load. The load method takes the FXML file to load, and the controller
 * to create and associate with the FXML file.
 */
public class GuiceFXMLLoader {

  private final Injector injector;

  @Inject
  public GuiceFXMLLoader(Injector injector) {
    this.injector = injector;
  }

  // Load some FXML file, using the supplied Controller, and return the
  // instance of the initialized controller...?
  public Parent load(URL url) {
    FXMLLoader loader = new FXMLLoader(url,
        injector.getInstance(ResourceBundle.class));
    loader.setControllerFactory(controllerClass -> {
      if (controllerClass == null) {
        return null;
      }
      Object instance = this.injector.getInstance(controllerClass);
      loader.getNamespace().put("controller", instance);
      return instance;
    });
    try {
      return (Parent) loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
