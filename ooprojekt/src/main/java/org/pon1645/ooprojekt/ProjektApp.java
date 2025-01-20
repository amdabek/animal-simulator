package org.pon1645.ooprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pon1645.ooprojekt.presenter.MenuPresenter;
import org.pon1645.ooprojekt.presenter.SimulationPresenter;

import java.io.IOException;

public class ProjektApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        MenuPresenter presenter = fxmlLoader.getController();
        stage.setTitle("Darwin World");
        stage.setScene(scene);
        stage.setOnHidden(e -> presenter.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}