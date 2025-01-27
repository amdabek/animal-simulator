package org.ponmaj.ooprojekt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void testAdd() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(-1, 4);
        Vector2d result = v1.add(v2);
        assertEquals(new Vector2d(1, 7), result);
    }

    @Test
    void testSubtract() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(-1, 4);
        Vector2d result = v1.subtract(v2);
        assertEquals(new Vector2d(3, -1), result);
    }

    @Test
    void testEqualsAndHashCode() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(2, 4);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);

        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1.hashCode(), v3.hashCode());
    }

    @Test
    void testToString() {
        Vector2d v = new Vector2d(-2, 5);
        assertEquals("(-2,5)", v.toString());
    }
}