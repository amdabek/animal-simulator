package org.pon1645.ooprojekt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SimulationEngine implements IObserver, Callable<Void> {
    private final GlobeMap map;
    private final List<Animal> animals = new ArrayList<>();
    //private boolean running = false;
    private final long stepDelay = 500;

    public SimulationEngine(GlobeMap map) {
        this.map = map;
    }

    public void generateInitialPlantsAndAnimals() {
        for (int i = 0; i < map.getConfig().initialPlants; i++) {
            map.spawnSinglePlantEquator();
        }
        for (int i = 0; i < map.getConfig().initialAnimals; i++) {
            int x=(int)(Math.random()*map.getWidth());
            int y=(int)(Math.random()*map.getHeight());
            addAnimal(new Vector2d(x,y));
        }
    }

    public void addAnimal(Vector2d pos) {
        Animal a = new Animal(map, pos);
        a.addObserver(this);
        animals.add(a);
    }

    public void run(int days) throws InterruptedException {
        for(int i=0; i<days; i++){
            doOneDay();
            Thread.sleep(stepDelay);
        }
    }

    public void doOneDay() {
        rotateAll();
        moveAll();
        reproduceAll();
        checkEating();
        removeDeadAnimals();
        map.growPlants();
    }

    private void rotateAll() {
        for(Animal a : animals){
            a.rotateAccordingToGene();
        }
    }

    private void moveAll() {
        for(Animal a : animals){
            a.move(MoveDirection.FORWARD);
            a.subtractEnergy(1);
        }
    }

    private void reproduceAll() {
        List<Animal> newAnimals = new ArrayList<>();
        for (Animal a : animals) {
            for (Animal b : animals) {
                if (a != b && a.getPosition().equals(b.getPosition())) {
                    if (!a.isDead() && !b.isDead()) {
                        if (a.getEnergy() > 0 && b.getEnergy() > 0) {
                            Animal child = a.reproduceWith(b);
                            child.addObserver(this);
                            newAnimals.add(child);
                        }
                    }
                }
            }
        }
        animals.addAll(newAnimals);
    }

    private void checkEating() {
        for(Animal a : animals){
            if(map.hasGrassAt(a.getPosition())){
                a.addEnergy(map.getConfig().plantEnergy);
                map.removeGrassAt(a.getPosition());
            }
        }
    }

    private void removeDeadAnimals() {
        animals.removeIf(Animal::isDead);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object element) {
        if(element instanceof Animal an) {
            List<Animal> oldList = map.getAnimalsAt(oldPosition);
            if(oldList != null) {
                oldList.remove(an);
                if(oldList.isEmpty()) {
                    map.removeAnimal(an);
                }
            }
            List<Animal> newList = map.getAnimalsAt(newPosition);
            if(newList == null){
                newList = new ArrayList<>();
                map.placeAnimal(an);
            } else {
                newList.add(an);
            }
        }
    }

    @Override
    public Void call() throws InterruptedException {
        run(1000);
        return null;
    }

    public List<Animal> getAnimals() { return animals; }

    public GlobeMap getMap() { return map; }
}
