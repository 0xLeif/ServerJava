package client.entities.objects;

import client.entities.EntityType;
import client.entities.StageEntity;
import client.interfaces.Solid;
import client.interfaces.Updateable;

import java.awt.*;

import source.State;

/**
 * Created by zeriksen on 7/1/2014.
 */
public class PlayerBlock extends StageEntity implements Updateable, Solid {
	private static final int DEFAULT_SPEED = 100;
	private static final int GRAVITY = 1300;
	private static final int DEFAULT_JUMP_POWER = 500;
	private static final int DEFUALT_ACCELERATION_X_SPEED = 10;
	private static int jumpPower = DEFAULT_JUMP_POWER;
	private Byte id;
	private double speed = DEFAULT_SPEED;
	protected boolean onSurface, doJump;
	private boolean start = true;
	private Color color = Color.orange;
	private double yv;
	private double nextSpeed;
	private int deathCount = 0;

	public PlayerBlock(double x, double y) {// TODO Hold jump
		super(x, y, 10, 10, EntityType.player);
	}

	public PlayerBlock(double x, double y, Color c) {
		super(x, y, 10, 10, EntityType.player);
		color = c;
	}

	@Override
	public State getState() {
		State s = new State(id, x, y, width, height, EntityType.player);
		return s;
	}

	public void giveColor(Color c) {
		color = c;
	}

	public boolean toJump() {
		return doJump;
	}

	public void onSurface(double d) {
		onSurface = true;
		yv = 0;
		y = d - 9;
	}

	public void offSurface() {
		onSurface = false;
	}

	public void start() {
		setStart(false);
	}

	public void jump() {
		if (onSurface) {
			doJump = true;
			onSurface = false;
		}
	}

	public boolean isJumping() {
		return yv < 0;
	}

	private void doJump() {
		if (onSurface) {
			yv = -jumpPower;
		}
		doJump = false;
	}

	public void giveId(Byte i) {
		id = i;
	}

	@Override
	public Rectangle getBounds() {
		return getBounds(0, 0);
	}

	public Rectangle getBounds(int dx, int dy) {
		return new Rectangle((int) x + dx, (int) y + dy, 10, 10);
	}

	public void sendKey(String s) {
		if (s.contains("w")) {
			y -= 100;
		} else if (s.contains("s")) {
			y += 100;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PlayerBlock that = (PlayerBlock) o;
		return color == that.color;
	}

	@Override
	public void update(double deltaTime) {
		if (doJump)
			doJump();
		x += speed * deltaTime;
		y += yv * deltaTime;
		yv += GRAVITY * deltaTime;
		speed += DEFUALT_ACCELERATION_X_SPEED * deltaTime;
	}

	@Override
	public void paint(Graphics g) {
		paint(g, 0, 0);
	}

	@Override
	public void paint(Graphics g, int dx, int dy) {
		g.setColor(color);
		g.fillRect((int) x + dx, (int) y + dy, 10, 10);
		g.setColor(Color.black);
	}

	public Color getColor() {
		return color;
	}

	public boolean isStart() {
		return start;
	}

	public void setState(State s) {
		x = s.getX();
		y = s.getY();
	}

	public void setStart(boolean start) {
		this.start = start;
	}
	
	public boolean isOnSurface(){
		return onSurface;
	}

	public void respawn(int startY) {
		speed = DEFAULT_SPEED;
		deathCount++;
		y = startY - 10;
		x = 10;
	}

	public int getDeathCount() {
		return deathCount;
	}

}
