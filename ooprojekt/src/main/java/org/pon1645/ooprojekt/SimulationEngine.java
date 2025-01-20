package org.pon1645.ooprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class SimulationEngine implements IObserver, Callable<Void> {
    private final GlobeMap map;
    private final List<Animal> animals = new ArrayList<>();
    private int currentDay = 0;

    private long totalDeadAnimals = 0;
    private long totalDeadAnimalsLifeSpan = 0;

    public int getPlantCount() {
        return plantCount;
    }

    public int getFreeFields() {
        return freeFields;
    }

    public List<String> getMostPopularGenomes() {
        return mostPopularGenomes;
    }

    public double getAvgEnergy() {
        return avgEnergy;
    }

    public double getAvgLifeSpan() {
        return avgLifeSpan;
    }

    public double getAvgChildren() {
        return avgChildren;
    }

    private int plantCount = 0;
    private int freeFields = 0;
    private List<String> mostPopularGenomes = new ArrayList<>();
    private double avgEnergy;
    private double avgLifeSpan;
    private double avgChildren;

    public SimulationEngine(GlobeMap map) {
        this.map = map;
    }

    public int getCurrentDay() { return currentDay; }

    public void generateInitialPlantsAndAnimals() {
        for (int i = 0; i < map.getConfig().initialPlants; i++) {
            map.spawnSinglePlantEquator();
        }
        for (int i = 0; i < map.getConfig().initialAnimals; i++) {
            int x = (int)(Math.random() * map.getWidth());
            int y = (int)(Math.random() * map.getHeight());
            addAnimal(new Vector2d(x,y));
        }
    }

    public void addAnimal(Vector2d pos) {
        Animal a = new Animal(map, pos, currentDay);
        a.addObserver(this);
        animals.add(a);
    }

    public void run(int days) throws InterruptedException {
        for(int i = 0; i < days; i++){
            doOneDay();
            Thread.sleep(500);
        }
    }

    public void doOneDay() {

        map.growPlants();
        checkEating();
        rotateAll();
        moveAll();
        reproduceAll();
        removeDeadAnimals();
        updateStats();
        currentDay++;
    }

    private void rotateAll() {
        for(Animal a : animals) {
            a.rotateAccordingToGene();
        }
    }

    private void moveAll() {
        for(Animal a : animals) {
            a.move(MoveDirection.FORWARD);
            a.subtractEnergy(1);
        }
    }

    private void checkEating() {
        for(Animal a : animals) {
            if(map.hasGrassAt(a.getPosition())){
                a.addEnergy(map.getConfig().plantEnergy);
                a.incrementPlantsEaten();
                map.removeGrassAt(a.getPosition());
            }
        }
    }

    private void reproduceAll() {
        List<Animal> newAnimals = new ArrayList<>();
        int minEnergy = map.getConfig().minEnergyToReproduce;

        for (int i = 0; i < animals.size(); i++) {
            Animal a = animals.get(i);
            if (a.isDead()) continue;

            for (int j = i+1; j < animals.size(); j++) {
                Animal b = animals.get(j);
                if (b.isDead()) continue;

                if (a.getPosition().equals(b.getPosition())) {
                    if(a.getEnergy() >= minEnergy && b.getEnergy() >= minEnergy) {
                        Animal child = a.reproduceWith(b, currentDay);
                        child.addObserver(this);
                        newAnimals.add(child);
                    }
                }
            }
        }
        animals.addAll(newAnimals);
    }

    private void removeDeadAnimals() {
        List<Animal> toRemove = new ArrayList<>();
        for(Animal a : animals) {
            if(a.isDead()) {
                if(a.getDeathDay() < 0) {
                    a.setDeathDay(currentDay);
                    totalDeadAnimals++;
                    totalDeadAnimalsLifeSpan += (a.getDeathDay() - a.getBirthDay());
                }
                toRemove.add(a);
                map.removeAnimal(a);
            }
        }
        animals.removeAll(toRemove);
    }


    private void updateStats() {

        plantCount = map.getGrasses().size();

        int width = map.getWidth();
        int height = map.getHeight();
        freeFields = 0;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Vector2d pos = new Vector2d(x,y);
                boolean hasAnimal = map.hasAnimalAt(pos);
                boolean hasGrass = map.hasGrassAt(pos);
                if(!hasAnimal && !hasGrass) {
                    freeFields++;
                }
            }
        }

        Map<String,Integer> genotypeCount = new HashMap<>();
        for(Animal a : animals){
            if(!a.isDead()){
                String genotypeStr = a.getGenes().toString();
                genotypeCount.put(genotypeStr, genotypeCount.getOrDefault(genotypeStr, 0) + 1);
            }
        }

        int maxCount = 0;
        for(var e : genotypeCount.entrySet()){
            if(e.getValue() > maxCount){
                maxCount = e.getValue();
            }
        }

        mostPopularGenomes = new ArrayList<>();
        for(var e : genotypeCount.entrySet()){
            if(e.getValue() == maxCount){
                mostPopularGenomes.add(e.getKey());
            }
        }

        //Średni poziom energii żyjących zwierząt
        int livingCount = 0;
        int totalEnergy = 0;
        for(Animal a : animals){
            if(!a.isDead()){
                livingCount++;
                totalEnergy += a.getEnergy();
            }
        }
        avgEnergy = (livingCount == 0) ? 0 : (double)totalEnergy / livingCount;

        //Średnia długość życia martwych zwierząt
        avgLifeSpan = (totalDeadAnimals == 0) ? 0 :
                (double) totalDeadAnimalsLifeSpan / totalDeadAnimals;

        // Średnia liczba dzieci
        int totalChildrenOfLiving = 0;
        for(Animal a : animals){
            if(!a.isDead()){
                totalChildrenOfLiving += a.getChildrenCount();
            }
        }
        avgChildren = (livingCount == 0) ? 0 :
                (double) totalChildrenOfLiving / livingCount;


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
