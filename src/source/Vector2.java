package source;

import java.io.Serializable;


public class Vector2 implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private double x;
    private double y;
	protected Vector2(){}
	private Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector2(double theta) {
		this.x = Math.cos(theta);
		this.y = Math.sin(theta);
	}
	public Vector2(Vector2 other) {
		this(other.x, other.y);
	}

	double length() {
		return Math.sqrt((x*x)+(y*y));
	}
	
	public double theta() {
		return Math.atan2(y, x);
	}
	
	public void normalize() {
		double len = length();
		if (len == 0) return;
		 x /= len;
		 y /= len;
	}
	
	public void add(Vector2 other) {
		x+=other.x;
		y+=other.y;
	}
	
	public void mul(double scalar){
		x*=scalar;
		y*=scalar;
	}
	
}
