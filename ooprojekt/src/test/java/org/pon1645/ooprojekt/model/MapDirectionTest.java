package org.pon1645.ooprojekt.model;


import org.junit.jupiter.api.Test;
import org.pon1645.ooprojekt.model.MapDirection;
import org.pon1645.ooprojekt.model.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void testNext() {
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTH.next());
        assertEquals(MapDirection.NORTHWEST, MapDirection.WEST.next());
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTHEAST.next());
    }

    @Test
    void testPrevious() {
        assertEquals(MapDirection.NORTHWEST, MapDirection.NORTH.previous());
        assertEquals(MapDirection.SOUTHEAST, MapDirection.SOUTH.previous());
        assertEquals(MapDirection.WEST, MapDirection.NORTHWEST.previous());
    }

    @Test
    void testOpposite() {
        assertEquals(MapDirection.SOUTH, MapDirection.NORTH.opposite());
        assertEquals(MapDirection.WEST, MapDirection.EAST.opposite());
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTHEAST.opposite());
    }

    @Test
    void testToUnitVector() {
        assertEquals(new Vector2d(0, 1), MapDirection.NORTH.toUnitVector());
        assertEquals(new Vector2d(1, 1), MapDirection.NORTHEAST.toUnitVector());
        assertEquals(new Vector2d(-1, -1), MapDirection.SOUTHWEST.toUnitVector());
    }
}
