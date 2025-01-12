package org.pon1645.ooprojekt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements IElements {
    private Vector2d position;
    private MapDirection direction;
    private final GlobeMap map;

    private int energy;
    private final int startEnergy;

    private final Genes genes;
    private final List<IObserver> observers = new ArrayList<>();

    public Animal(GlobeMap map, Vector2d initialPos) {
        this.map = map;
        this.position = initialPos;
        this.direction = randomOrientation();
        int configStartEnergy = map.getConfig().startEnergy;
        this.energy = configStartEnergy;
        this.startEnergy = configStartEnergy;

        this.genes = new Genes();
    }

    public Animal(GlobeMap map, Vector2d initialPos, int childEnergy) {
        this.map = map;
        this.position = initialPos;
        this.direction = randomOrientation();
        this.energy = childEnergy;

        this.startEnergy = map.getConfig().startEnergy;

        this.genes = new Genes();
    }

    public void addEnergy(int value) {
        energy += value;
    }

    public void subtractEnergy(int value) {
        energy -= value;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public void move(MoveDirection dir) {
        switch (dir) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> tryToMoveForward();
            case BACKWARD -> tryToMoveBackward();
        }
    }

    private void tryToMoveForward() {
        Vector2d oldPos = position;
        Vector2d potentialNew = position.add(direction.toUnitVector());
        Vector2d actualNewPos = map.wrapPositionIfNeeded(oldPos, potentialNew, this);

        if (!oldPos.equals(actualNewPos)) {
            this.position = actualNewPos;
            positionChanged(oldPos, actualNewPos);
        }
    }

    private void tryToMoveBackward() {
        Vector2d oldPos = position;
        Vector2d potentialNew = position.subtract(direction.toUnitVector());
        Vector2d actualNewPos = map.wrapPositionIfNeeded(oldPos, potentialNew, this);

        if (!oldPos.equals(actualNewPos)) {
            this.position = actualNewPos;
            positionChanged(oldPos, actualNewPos);
        }
    }

    // Blokada przy wyjściu za górę/dół
    public void reverseDirection() {
        this.direction = this.direction.opposite();
    }

    // Losuje gen i obraca się zgodnie z nim
    public void rotateAccordingToGene() {
        int rotationGene = genes.getRandomGene();
        for (int i = 0; i < rotationGene; i++) {
            this.direction = this.direction.next();
        }
    }


    public Animal reproduceWith(Animal partner) {
        double fraction = map.getConfig().reproductionEnergyFraction;

        int fatherContribution = (int) (this.energy * fraction);
        int motherContribution = (int) (partner.energy * fraction);

        this.energy -= fatherContribution;
        partner.energy -= motherContribution;

        int childEnergy = fatherContribution + motherContribution;
        double ratio = (childEnergy == 0)
                ? 0.5
                : (double) fatherContribution / (childEnergy);

        Genes childGenes = new Genes(this.genes, partner.genes, ratio);
        // Mutacja
        childGenes.mutate(map.getConfig().mutationVariant);

        return new Animal(map, this.position, childEnergy);
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    private void positionChanged(Vector2d oldPos, Vector2d newPos) {
        for (IObserver obs : observers) {
            obs.positionChanged(oldPos, newPos, this);
        }
    }

    private MapDirection randomOrientation() {
        return MapDirection.fromInt(new Random().nextInt(8));
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isDead() {
        return energy <= 0;
    }
}
