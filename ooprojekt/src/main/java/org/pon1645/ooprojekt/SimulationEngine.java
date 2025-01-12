package org.pon1645.ooprojekt;

import java.util.*;

public class SimulationEngine {
    private final GlobeMap map;
    private final List<Animal> animals = new ArrayList<>();

    private final int moveCost = 1;                // utrata energii za 1 ruch
    private final int minReproductionEnergy = 8;   // min energia do rozmnażania

    public SimulationEngine(GlobeMap map) {
        this.map = map;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void run(int days) {
        for (int day = 0; day < days; day++) {
            System.out.println("\n=== Dzień " + day + " ===");

            // Jedzenie
            eatGrassPhase();

            // Rozmnażanie
            reproducePhase();

            // Usunięcie martwych
            removeDeadAnimals();

            // Wyświetlanie stanu
            printAnimalsState();

            // ruch
            for (Animal animal : animals) {
                animal.rotateAccordingToGene();
                animal.move(MoveDirection.FORWARD);
                animal.subtractEnergy(moveCost);
            }
        }
    }

    private void eatGrassPhase() {
        Map<Vector2d, List<Animal>> groupedByPos = new HashMap<>();
        for (Animal a : animals) {
            groupedByPos
                    .computeIfAbsent(a.getPosition(), k -> new ArrayList<>())
                    .add(a);
        }

        for (Map.Entry<Vector2d, List<Animal>> entry : groupedByPos.entrySet()) {
            Vector2d pos = entry.getKey();
            if (map.hasGrassAt(pos)) {
                // Wybór najsilniejszego do zjedzenia trway
                List<Animal> animalsHere = entry.getValue();
                animalsHere.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
                Animal strongest = animalsHere.get(0);

                strongest.addEnergy(Grass.getEnergyValue());
                System.out.println("  " + strongest + " zjada trawę na polu " + pos);

                map.removeGrassAt(pos);
            }
        }
    }

    private void reproducePhase() {
        Map<Vector2d, List<Animal>> groupedByPos = new HashMap<>();
        for (Animal a : animals) {
            groupedByPos
                    .computeIfAbsent(a.getPosition(), k -> new ArrayList<>())
                    .add(a);
        }

        for (Map.Entry<Vector2d, List<Animal>> entry : groupedByPos.entrySet()) {
            List<Animal> animalsHere = entry.getValue();
            if (animalsHere.size() > 1) {
                animalsHere.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
                // co dwie sztuki
                for (int i = 0; i < animalsHere.size() - 1; i += 2) {
                    Animal first = animalsHere.get(i);
                    Animal second = animalsHere.get(i+1);

                    if (first.getEnergy() >= minReproductionEnergy
                            && second.getEnergy() >= minReproductionEnergy) {
                        Animal child = first.reproduceWith(second);
                        animals.add(child);
                        System.out.println("  >>> Narodziny dziecka na polu "
                                + child.getPosition()
                                + " z rodziców o energii "
                                + first.getEnergy() + " i " + second.getEnergy());
                    }
                }
            }
        }
    }

    private void removeDeadAnimals() {
        int before = animals.size();
        animals.removeIf(Animal::isDead);
        int after = animals.size();
        if (after < before) {
            System.out.println("  >>> Usunięto " + (before - after) + " martwych zwierząt.");
        }
    }

    private void printAnimalsState() {
        for (Animal a : animals) {
            System.out.println("  " + a + " -> pozycja: "
                    + a.getPosition()
                    + ", energia=" + a.getEnergy());
        }
    }
}
