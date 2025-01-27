package org.ponmaj.ooprojekt.model;

public interface IObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object element);
}
