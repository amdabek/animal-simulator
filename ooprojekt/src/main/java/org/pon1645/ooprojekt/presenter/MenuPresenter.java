package org.pon1645.ooprojekt.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPresenter implements Initializable {
    public TextField height;
    public TextField width;
    public RadioButton equatorial;
    public ToggleGroup plants;
    public RadioButton jungle;
    public RadioButton random;
    public ToggleGroup mutation;
    public RadioButton cautious;
    public TextField plantNumber;
    public TextField energyPerMeal;
    public TextField plantsPerDay;
    public TextField animals;
    public TextField energy;
    public TextField fullEnergy;
    public TextField reproduceEnergy;
    public TextField minMutations;
    public TextField maxMutations;
    public TextField genomeLength;
    public Label message;

    public void onSaveButtonClick(ActionEvent actionEvent) {
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation " + "simulationEngine.size()");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private void setError(String text) {
        message.setText(text);
        message.setStyle("-fx-text-fill: red");
    }

    public void onStartButtonClick(ActionEvent actionEvent) {
        Stage newSimulation = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = null;
        try {
            viewRoot = loader.load();
        } catch (IOException e) {
            setError(e.getMessage());
        }
        //SimulationPresenter presenter = loader.getController();
        //presenter.setWorldMap(worldMap);
        configureStage(newSimulation, viewRoot);
        newSimulation.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}