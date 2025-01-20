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

    public Animal(GlobeMap map, Vector2d pos) {
        this.map = map;
        position = pos;
        direction = randomOrientation();
        energy = map.getConfig().startEnergy;
        startEnergy = map.getConfig().startEnergy;
        genes = new Genes(map.getConfig());
        map.placeAnimal(this);
    }

    public Animal(GlobeMap map, Vector2d pos, int childEnergy, Genes childGenes) {
        this.map = map;
        position = pos;
        direction = randomOrientation();
        energy = childEnergy;
        startEnergy = map.getConfig().startEnergy;
        genes = childGenes;
        map.placeAnimal(this);
    }

    @Override
    public boolean isMovable(){
        return true;
    }

    @Override
    public void move(MoveDirection dir){
        switch(dir){
            case LEFT->direction = direction.previous();
            case RIGHT->direction = direction.next();
            case FORWARD->forward();
            case BACKWARD->backward();
        }
    }

    private void forward(){
        Vector2d old = position;
        Vector2d pot = position.add(direction.toUnitVector());
        Vector2d n = map.wrapPositionIfNeeded(old, pot, this);
        if(!old.equals(n)){
            position = n;
            positionChanged(old, n);
        }
    }

    private void backward(){
        Vector2d old = position;
        Vector2d pot = position.subtract(direction.toUnitVector());
        Vector2d n = map.wrapPositionIfNeeded(old, pot, this);
        if(!old.equals(n)){
            position = n;
            positionChanged(old, n);
        }
    }

    public void reverseDirection(){
        direction = direction.opposite();
    }

    public void rotateAccordingToGene(){
        int g = genes.getRandomGene();
        for(int i=0; i<g; i++){
            direction = direction.next();
        }
    }

    public Animal reproduceWith(Animal partner){
        double frac = map.getConfig().reproductionEnergyFraction;
        int f = (int)(energy * frac);
        int m = (int)(partner.energy * frac);
        energy -= f;
        partner.energy -= m;
        int childEnergy = f + m;
        double ratio = (childEnergy==0)?0.5: (double)f/childEnergy;
        Genes childGenes = new Genes(this.genes, partner.genes, ratio, map.getConfig());
        childGenes.mutate(map.getConfig());
        return new Animal(map, position, childEnergy, childGenes);
    }

    public void addEnergy(int v){
        energy += v;
    }

    public void subtractEnergy(int v){
        energy -= v;
    }

    public int getEnergy(){
        return energy;
    }

    public boolean isDead(){
        return energy <= 0;
    }

    @Override
    public void addObserver(IObserver o){
        observers.add(o);
    }

    private void positionChanged(Vector2d oldPos, Vector2d newPos){
        for(IObserver o : observers){
            o.positionChanged(oldPos, newPos, this);
        }
    }

    @Override
    public Vector2d getPosition(){
        return position;
    }

    public MapDirection getDirection() { return direction; }

    public Genes getGenes() { return genes; }

    private MapDirection randomOrientation(){
        return MapDirection.values()[new Random().nextInt(8)];
    }

}
