package client.entities;

import client.interfaces.Visible;
import source.State;
import source.Vector2;

import java.awt.*;
import java.io.Serializable;

public class StageEntity extends Vector2 implements Visible, Serializable {
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected EntityType et;

	protected StageEntity(double x, double y, int width, int height,
			EntityType et) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.et = et;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void paint(Graphics g) {
		paint(g, 0, 0, Color.white);
	}

	public void paint(Graphics g, int dx, int dy, Color c) {
		g.setColor(c);
		g.drawOval((int) x + dx, (int) y + dy, width, height);
		g.setColor(Color.white);
	}

	public void paint(Graphics g, int dx, int dy) {
		g.setColor(Color.white);
		g.drawOval((int) x + dx, (int) y + dy, width, height);
	}

	public State getState() {
		return new State(new Byte((byte) 0), x, y, width, height, et);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public EntityType getEt() {
		return et;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	@Override
	public String toString() {
		return "StageEntity{" + "x=" + x + ", y=" + y + ", width=" + width
				+ ", height=" + height + ", et=" + et + '}';
	}
}