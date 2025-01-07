package org.pon1645.ooprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
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

    public void onSaveButtonClick(ActionEvent actionEvent) {
    }

    public void onStartButtonClick(ActionEvent actionEvent) {
    }

    //private TextFormatter<Integer> integerTextFormatter(int min, int max) {
    //    return new TextFormatter<>(change -> {

//        });
  //  }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}