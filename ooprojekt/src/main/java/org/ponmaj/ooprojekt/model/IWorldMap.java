package org.ponmaj.ooprojekt.model;

public interface IWorldMap {
    boolean canMoveTo(Vector2d position);
    int getWidth();
    int getHeight();
}
