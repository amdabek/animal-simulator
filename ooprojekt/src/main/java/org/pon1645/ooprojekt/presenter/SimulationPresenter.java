package org.pon1645.ooprojekt.presenter;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.Image;
import org.pon1645.ooprojekt.*;

import java.util.List;
import java.util.Objects;

public class SimulationPresenter implements IObserver {
    public CheckBox debug;
    public GridPane mapGrid;
    public Label message;

    private GlobeMap globeMap;

    public void setGlobeMap(GlobeMap globeMap) {
        this.globeMap = globeMap;
        this.message.setStyle("-fx-text-fill: black");
        drawMap();
   }

    public void drawMap() {
        clearGrid();
        int width = globeMap.getWidth();
        int height = globeMap.getHeight();
        float CELL_WIDTH = 50;
        float CELL_HEIGHT = 50;
        for (int i = 0; i <= height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i = 0; i <= width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                Node cell;
                if (i == 0 && j == 0)
                    cell = new Label("y\\x");
                else if (i == 0)
                    cell = new Label(j+"");
                else if (j == 0)
                    cell = new Label(i+"");
                else {
                    cell = new ImageView();
                    ImageView cellImageView = (ImageView) cell;
                    if (globeMap.hasGrassAt(new Vector2d(j, i)))
                        cellImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grass.png"))));
                    cellImageView.setFitWidth(CELL_WIDTH);
                    cellImageView.setFitHeight(CELL_HEIGHT);
                }
                mapGrid.add(cell, j, i);
                GridPane.setHalignment(cell, HPos.CENTER);
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object element) {
        Platform.runLater(this::drawMap);
    }
}
