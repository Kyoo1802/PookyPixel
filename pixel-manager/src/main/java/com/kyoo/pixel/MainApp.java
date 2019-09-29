package com.kyoo.pixel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp extends Application {
    private static final Logger LOGGER = LogManager.getLogger(MainApp.class);
    private final Injector injector = Guice.createInjector(new SceneModule());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.debug("Starting your application0.");
        LOGGER.warn("Starting your application1.");
        LOGGER.info("Starting your application2.");

        GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);

        Parent root = loader.load(getClass().getResource("views/scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

}