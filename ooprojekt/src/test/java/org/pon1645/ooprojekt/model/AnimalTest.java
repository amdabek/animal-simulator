package org.pon1645.ooprojekt.model;

import org.junit.jupiter.api.Test;
import org.pon1645.ooprojekt.SimulationConfig;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void testAnimalCreationAndBasicMovement() {
        SimulationConfig config = new SimulationConfig();
        config.width = 10;
        config.height = 10;
        config.startEnergy = 50;

        GlobeMap map = new GlobeMap(config);
        Animal animal = new Animal(map, new Vector2d(5, 5), 0);

        assertEquals(new Vector2d(5, 5), animal.getPosition());
        assertEquals(50, animal.getEnergy());

        animal.move(MoveDirection.FORWARD);
        Vector2d expectedForward = new Vector2d(5, 6);

        Vector2d newPos = animal.getPosition();
        assertNotEquals(new Vector2d(5, 5), newPos, "Po ruchu FORWARD pozycja powinna się zmienić.");


        animal.move(MoveDirection.BACKWARD);
        assertEquals(new Vector2d(5, 5), animal.getPosition(), "Zwierzę powinno wrócić do pozycji początkowej.");
    }

    @Test
    void testEnergyUsageAndDeath() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.startEnergy = 5;
        GlobeMap map = new GlobeMap(config);

        Animal animal = new Animal(map, new Vector2d(2, 2), 0);
        animal.subtractEnergy(3);
        assertFalse(animal.isDead());
        animal.subtractEnergy(2);
        assertTrue(animal.isDead(), "Po zużyciu całej energii zwierzę powinno być martwe.");
    }

    @Test
    void testReproduction() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.startEnergy = 30;
        config.reproductionEnergyFraction = 0.4;
        config.genomeLength = 6;
        GlobeMap map = new GlobeMap(config);

        Animal parentA = new Animal(map, new Vector2d(2, 2), 0);
        Animal parentB = new Animal(map, new Vector2d(2, 2), 0);
        Animal child = parentA.reproduceWith(parentB, 1);

        assertNotNull(child);
        int expectedEnergyA = (int) (30 - 30 * 0.4);
        int expectedEnergyB = (int) (30 - 30 * 0.4);
        assertEquals(expectedEnergyA, parentA.getEnergy());
        assertEquals(expectedEnergyB, parentB.getEnergy());

        // 40% A + 40% B = 24
        assertEquals(24, child.getEnergy());
        assertEquals(parentA.getPosition(), child.getPosition());
        assertEquals(1, parentA.getChildrenCount());
        assertEquals(1, parentB.getChildrenCount());
    }

    @Test
    void testPlantsEatenCounter() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.startEnergy = 10;
        config.plantEnergy = 5;

        GlobeMap map = new GlobeMap(config);

        Animal a = new Animal(map, new Vector2d(2, 2), 0);
        a.incrementPlantsEaten();
        a.incrementPlantsEaten();
        assertEquals(2, a.getPlantsEaten());
    }
}

