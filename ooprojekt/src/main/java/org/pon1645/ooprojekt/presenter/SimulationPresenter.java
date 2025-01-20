package org.pon1645.ooprojekt.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import org.pon1645.ooprojekt.*;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SimulationPresenter implements IObserver {
    public CheckBox debug;
    public GridPane mapGrid;
    public Label animalAmount;
    public Label plantAmount;
    public Label freeSpaces;
    public Label averageEnergy;
    public Label averageLifespan;
    public Label averageChildren;
    public ImageView selectedImage;
    public Label selectedGenome;
    public Label selectedActiveGenes;
    public Label selectedEnergy;
    public Label selectedGrass;
    public Label selectedChildren;
    public Label selectedRelatives;
    public Label selectedAge;
    public Button pauseButton;
    public VBox mostPopularGenotypes;
    private Pane selectedPane;
    private SimulationEngine simulation;
    private Animal selectedAnimal;

    private Future<Void> future;
    private ExecutorService executorService;

    public void startSimulation(SimulationEngine simulation, ExecutorService executorService) {
        this.simulation = simulation;
        this.executorService = executorService;
        future = executorService.submit(simulation);
        for (Animal animal : simulation.getAnimals())
            animal.addObserver(this);
        update();
   }

    public void drawMap() {
        clearGrid();
        GlobeMap globeMap = simulation.getMap();
        int width = globeMap.getWidth();
        int height = globeMap.getHeight();
        float AREA = 80*13;
        float size = Math.min(AREA/(width+1), AREA/(height+1));
        for (int i = 0; i <= height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(size));
        }
        for (int i = 0; i <= width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(size));
        }
        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                Node cell;
                if (i == 0 && j == 0)
                    cell = new Label("y\\x");
                else if (i == 0)
                    cell = new Label(j-1+"");
                else if (j == 0)
                    cell = new Label(i-1+"");
                else {
                    cell = new StackPane();
                    StackPane cellPane = (StackPane) cell;
                    ImageView cellImageView = new ImageView();
                    cellPane.getChildren().add(cellImageView);
                    Vector2d location = new Vector2d(j-1, i-1);
                    String image = "";
                    if (globeMap.hasGrassAt(location))
                        image = "grass.png";
                    if (globeMap.hasAnimalAt(location)) {
                        List<Animal> animals = globeMap.getAnimalsAt(location);
                        if (animals.size() == 1) {
                            Animal animal = animals.getFirst();
                            cellPane.setOnMouseClicked(mouseEvent -> {
                                selectedAnimal = animal;
                                if (selectedPane != null)
                                    selectedPane.setStyle("-fx-border-width: 0px");
                                selectedPane = cellPane;
                                updateSelectedStats();
                            });
                            if (animal.getEnergy() >= globeMap.getConfig().minEnergyToReproduce)
                                image = "full.png";
                            else if (animal.getEnergy() <= globeMap.getConfig().startEnergy*0.10) {
                                image = "empty.png";
                            }
                            else image = "tired.png";
                        }
                        else if (animals.size() == 2 && animals.stream().allMatch(animal -> animal.getEnergy() >= globeMap.getConfig().minEnergyToReproduce))
                            image = "love.png";
                        else image = "fight.png";
                        if (debug.isSelected() && animals.stream().anyMatch(animal -> simulation.getMostPopularGenomes().stream().anyMatch(gene -> gene.equals(animal.getGenes().toString()))))
                            cellPane.setStyle("-fx-border-width: 4px; -fx-border-color: red");
                        if (animals.contains(selectedAnimal))
                            selectedPane = cellPane;
                    }
                    cellImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + image))));
                    cellImageView.setFitWidth(size);
                    cellImageView.setFitHeight(size);
                    if (debug.isSelected()) {
                        Label prefferedLabel = new Label();
                        if (globeMap.isPreferredField(location)) {
                            prefferedLabel.setText("P");
                            prefferedLabel.setStyle("-fx-text-fill: #006400");
                        }
                        else {
                            prefferedLabel.setText("NP");
                            prefferedLabel.setStyle("-fx-text-fill: red");
                        }
                        cellPane.getChildren().add(prefferedLabel);
                    }
                }
                mapGrid.add(cell, j, i);
                GridPane.setHalignment(cell, HPos.CENTER);
            }
        }
    }

    private void updateSelectedStats() {
        if (selectedAnimal != null) {
            selectedPane.setStyle("-fx-border-width: 4px; -fx-border-color: green");
            ImageView imageView = (ImageView) selectedPane.getChildren().getFirst();
            selectedImage.setImage(imageView.getImage());
            selectedGenome.setText(selectedAnimal.getGenes().toString());
            selectedEnergy.setText(String.valueOf(selectedAnimal.getEnergy()));
            selectedAge.setText(selectedAnimal.getDaysLived(simulation.getCurrentDay()) + " " + (selectedAnimal.isDead() ? "(Nie żyje)" : ""));
            selectedActiveGenes.setText(String.valueOf(selectedAnimal.getActivatedGeneIndex()));
            selectedChildren.setText(String.valueOf(selectedAnimal.getChildrenCount()));
            selectedGrass.setText(String.valueOf(selectedAnimal.getPlantsEaten()));
            selectedRelatives.setText(String.valueOf(selectedAnimal.getDescendantsCount()));
        }
    }

    private void updateStats() {
        animalAmount.setText(String.valueOf(simulation.getAnimals().size()));
        plantAmount.setText(String.valueOf(simulation.getPlantCount()));
        mostPopularGenotypes.getChildren().clear();
        for (String gene : simulation.getMostPopularGenomes())
            mostPopularGenotypes.getChildren().add(new Label(gene));
        freeSpaces.setText(String.valueOf(simulation.getFreeFields()));
        averageChildren.setText(String.valueOf(simulation.getAvgChildren()));
        averageEnergy.setText(String.valueOf(simulation.getAvgEnergy()));
        averageLifespan.setText(String.valueOf(simulation.getAvgLifeSpan()));
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void update() {
        drawMap();
        updateSelectedStats();
        updateStats();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object element) {
        Platform.runLater(this::update);
    }

    public void pause() {
        future.cancel(true);
        pauseButton.setStyle("-fx-text-fill: red");
    }

    public void onPauseButtonClicked(ActionEvent actionEvent) {
        if (future.isCancelled()) {
            future = executorService.submit(simulation);
            pauseButton.setStyle("-fx-text-fill: green");
        }
        else pause();
    }

    public void onDebugPressed(ActionEvent actionEvent) {
        update();
    }
}
