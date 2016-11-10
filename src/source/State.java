package source;

import client.entities.EntityType;

import java.io.Serializable;

/**
 * Created by zeriksen on 7/1/2014.
 */

public class State implements Serializable {
    private Byte id;
    private double x, y;
    private int w, h;
    private EntityType et;

    public State(Byte id, double x, double y, int width, int height, EntityType et) {
        this.id = id;
        this.x = x;
        this.y = y;
        w = width;
        h = height;
        this.et = et;
    }
    
    public Byte getID(){
    	return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public EntityType getEt() {
        return et;
    }

    @Override
    public String toString() {
        return id + "," + x +
                "," + y +
                "," + w +
                "," + h +
                "," + et;
    }
}