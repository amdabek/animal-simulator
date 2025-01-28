package org.ponmaj.ooprojekt.model;


import org.junit.jupiter.api.Test;
import org.ponmaj.ooprojekt.SimulationConfig;

import static org.junit.jupiter.api.Assertions.*;

class GlobeMapTest {

    @Test
    void testPlaceAnimalAndRemoveAnimal() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;

        GlobeMap map = new GlobeMap(config);
        Animal a = new Animal(map, new Vector2d(2,2), 0);

        assertTrue(map.hasAnimalAt(new Vector2d(2,2)));
        map.removeAnimal(a);
        assertFalse(map.hasAnimalAt(new Vector2d(2,2)));
    }

    @Test
    void testGrassSpawnAndRemove() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;

        GlobeMap map = new GlobeMap(config);
        Vector2d grassPos = new Vector2d(1,1);

        assertFalse(map.hasGrassAt(grassPos));
        map.spawnGrassAt(grassPos);
        assertTrue(map.hasGrassAt(grassPos));
        map.removeGrassAt(grassPos);
        assertFalse(map.hasGrassAt(grassPos));
    }

    @Test
    void testWrapPositionIfNeeded() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        GlobeMap map = new GlobeMap(config);

        Animal a = new Animal(map, new Vector2d(0, 0), 0);

        Vector2d wrapped1 = map.wrapPositionIfNeeded(
                a.getPosition(),
                a.getPosition().add(new Vector2d(-1,0)),
                a
        );
        assertEquals(new Vector2d(4, 0), wrapped1);

        Vector2d noWrap = map.wrapPositionIfNeeded(
                a.getPosition(),
                a.getPosition().add(new Vector2d(0,5)),
                a
        );
        assertEquals(a.getPosition(), noWrap);
    }

    @Test
    void testIsPreferredFieldEquator() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 10;
        config.plantGrowthVariant = PlantGrowthVariant.EQUATOR;
        GlobeMap map = new GlobeMap(config);

        assertTrue(map.isPreferredField(new Vector2d(2,4)));
        assertTrue(map.isPreferredField(new Vector2d(0,5)));
        assertFalse(map.isPreferredField(new Vector2d(1,3)));
        assertFalse(map.isPreferredField(new Vector2d(1,6)));
    }

    @Test
    void testIsPreferredFieldForestCreeping() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.plantGrowthVariant = PlantGrowthVariant.FOREST_CREEPING;
        GlobeMap map = new GlobeMap(config);

        assertFalse(map.isPreferredField(new Vector2d(2,2)));

        map.spawnGrassAt(new Vector2d(2,2));
        assertTrue(map.isPreferredField(new Vector2d(3,3)));
        assertFalse(map.isPreferredField(new Vector2d(0,0)));
    }

    @Test
    void testGrowPlants() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.plantGrowthVariant = PlantGrowthVariant.EQUATOR;
        config.plantsPerDay = 2;

        GlobeMap map = new GlobeMap(config);
        int before = map.getGrasses().size();
        map.growPlants();
        int after = map.getGrasses().size();

        assertEquals(before + 2, after);
    }
}
