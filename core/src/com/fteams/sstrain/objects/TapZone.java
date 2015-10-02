package com.fteams.sstrain.objects;

import com.badlogic.gdx.math.Vector2;

public class TapZone {

    Integer id;

    public boolean pressed;
    public boolean warn;

    Vector2 position = new Vector2();
    public float touchTime = -1f;

    public TapZone(float x, float y, int i) {
        this.id = i;
        this.position.x = x;
        this.position.y = y;
        pressed = false;
        warn = false;
    }

    public void update(float delta) {

    }

    public Integer getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }
}
