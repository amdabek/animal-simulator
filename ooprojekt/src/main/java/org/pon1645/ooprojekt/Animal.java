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

    private final int birthDay;
    private int deathDay = -1;
    private int childrenCount = 0;
    private int plantsEaten = 0;
    private int activatedGeneIndex = -1;
    private final List<Animal> children = new ArrayList<>();

    public Animal(GlobeMap map, Vector2d pos, int currentDay) {
        this.map = map;
        position = pos;
        direction = randomOrientation();
        energy = map.getConfig().startEnergy;
        startEnergy = map.getConfig().startEnergy;
        genes = new Genes(map.getConfig());
        this.birthDay = currentDay;
        map.placeAnimal(this);
    }

    public Animal(GlobeMap map, Vector2d pos, int childEnergy, Genes childGenes, int currentDay) {
        this.map = map;
        position = pos;
        direction = randomOrientation();
        energy = childEnergy;
        startEnergy = map.getConfig().startEnergy;
        genes = childGenes;
        this.birthDay = currentDay;
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
        int index = new Random().nextInt(genes.size());
        int geneVal = genes.getGene(index);
        this.activatedGeneIndex = index;
        for(int i=0; i<geneVal; i++){
            direction = direction.next();
        }
    }

    public Animal reproduceWith(Animal partner, int currentDay){
        double frac = map.getConfig().reproductionEnergyFraction;

        int f = (int)(energy * frac);
        int m = (int)(partner.energy * frac);

        this.energy -= f;
        partner.energy -= m;

        int childEnergy = f + m;
        double ratio = (childEnergy == 0) ? 0.5 : (double)f / childEnergy;

        Genes childGenes = new Genes(this.genes, partner.genes, ratio, map.getConfig());
        childGenes.mutate(map.getConfig());

        Animal child = new Animal(map, position, childEnergy, childGenes, currentDay);

        this.childrenCount++;
        partner.childrenCount++;

        this.children.add(child);
        partner.children.add(child);

        return child;
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

    public int getActivatedGeneIndex() {
        return activatedGeneIndex;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }

    public void incrementPlantsEaten() {
        this.plantsEaten++;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public int getDescendantsCount() {
        int count = 0;
        List<Animal> queue = new ArrayList<>(children);
        while(!queue.isEmpty()){
            Animal a = queue.remove(0);
            count++;
            queue.addAll(a.children);
        }
        return count;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public int getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(int day) {
        this.deathDay = day;
    }

    public int getDaysLived(int currentDay) {
        if (isDead() && deathDay >= 0) {
            return deathDay - birthDay;
        }
        return currentDay - birthDay;
    }

}
