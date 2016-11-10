package source;

import client.entities.StageEntity;
import client.entities.objects.GroundBlock;
import client.entities.objects.PlayerBlock;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class World implements Serializable {
	private HashMap<Byte, StageEntity> entities;
	protected HashMap<Byte, PlayerBlock> players;

	public World() {
		entities = new HashMap<Byte, StageEntity>();
		players = new HashMap<Byte, PlayerBlock>();
		buildWorld();
	}

	public WorldState getState() {
		Vector<State> s = new Vector<State>();
		try {
			// for (byte i = 0; i < entities.size(); i++) {
			// s.add(entities.get(i).getState(i));
			// }
			for (byte i = 0; i < players.size(); i++) {
				s.add(players.get(new Byte((byte) i)).getState());
			}
		} catch (Exception e) {
		}
		return new WorldState(s);

	}

	public void playerJump(byte b) {
		if (entities.containsKey(b)) {
			StageEntity temp = entities.get(b);
			if (temp instanceof PlayerBlock) {
				((PlayerBlock) temp).jump();
			}
		}
	}

	// public void clearEntites() {
	// entities = new HashMap<Byte, StageEntity>();
	// }
	//
	// public void clearPlayers() {
	// players = new HashMap<Byte, PlayerBlock>();
	// }

	private int maxX = -1;
	private GroundBlock highscore;
	private PlayerBlock highscoreHolder;

	public void update(double deltaTime) {// TODO jump past platform
		// for everything that can be updated, update it
		// perform collision detection
		for (Object o : entities.values()) {
			if (o instanceof Byte)
				System.out.println(o);
		}
		for (PlayerBlock pb : players.values()) {// each player
			int count = 0;
			try {
				for (StageEntity se : entities.values()) {// each object
					// perform collision resolution
					if (se instanceof GroundBlock) {// if ground
						GroundBlock gb = (GroundBlock) se;
						if (!pb.isJumping())
							if (pb.getBounds().intersects(gb.getBounds())) {
								if (gb.getX() > maxX) {
									maxX = (int) gb.getX();
									highscore = gb;
									highscoreHolder = pb;
								}
								pb.onSurface(gb.getY());// on surface
								break;// break
							} else {
								pb.offSurface();
							}
					}
					count++;
				}
			} catch (Exception e) {
				System.out.println(entities.get(count));
			}
			if (pb.getY() > 600) {
				pb.respawn(startY);
			}
			// break right to update
			pb.update(deltaTime);
		}

	}
	
	public int getHighscore(){
		return maxX;
	}

	public void paint(Graphics g) {
		paint(g, 0, 0);
	}

	public void paint(Graphics g, int dx, int dy) {
		try {
			for (StageEntity se : entities.values()) {
				if (highscore.equals(se))
					((GroundBlock) se).paintHighscore(g, dx, dy,
							highscoreHolder.getColor());
				else
					se.paint(g, dx, dy);
			}
			for (PlayerBlock p : players.values()) {
				p.paint(g, dx, dy);
			}
		} catch (Exception e) {

		}
	}

	public HashMap<Byte, StageEntity> getEntities() {
		return entities;
	}

	public HashMap<Byte, PlayerBlock> getPlayers() {
		return players;
	}

	public void removePlayer(Byte b) {
		players.remove(b);
	}

	public void addStageEntity(StageEntity se) {
		entities.put((byte) entities.size(), se);
	}

	public void setPlayers(WorldState ws) {
		System.out.println("World.setPlayers()");
		Vector<State> e = ws.getStates();
		for (State s : e) {
			System.out
					.println(s.getID() + " >" + players.get(s.getID()).getX());
			players.get(s.getID()).setState(s);
			System.out.println("  >" + players.get(s.getID()).getX());
		}
	}

	public void addPlayer(PlayerBlock pb) {
		players.put((byte) players.size(), pb);
	}

	public boolean hasPlayer(byte i) {
		return players.get(i) == null ? false : true;
	}

	private transient final int SPACING = -10;
	private transient final int HEIGHT = 80;
	private transient int LENGTH = 10;
	private int startY;

	public int getStartY() {
		return startY;
	}

	private void buildWorld() {
		boolean first = true;
		System.out.println("Building world...");
		int count = 0;
		int len = 0;
		int lastY = -1;
		int maxLen = -1;
		// for loop
		// for difficulty
		for (int x = 0; x < 100; x++) {
			len = getRand(7, LENGTH);
			if (first) {
				lastY = getRand(200, 500);
				startY = lastY;
				entities.put((byte) count, new GroundBlock(x, lastY, 10));
				maxLen = 10;
				first = false;
			} else {
				lastY = (int) (Math.random() * 100) % 2 == 0 ? getRand(lastY
						+ HEIGHT / 2, lastY + HEIGHT) : getRand(lastY - HEIGHT,
						lastY - HEIGHT / 2);
				lastY /= 10;
				lastY *= 10;
				if (lastY < 25) {
					lastY = getRand(lastY + HEIGHT / 2, lastY + HEIGHT);
				}
				if (lastY > 500) {
					lastY = getRand(lastY - HEIGHT, lastY - HEIGHT / 2);
				}
				entities.put((byte) count, new GroundBlock((maxLen * 10 * x)
						+ SPACING, lastY, len));
				if (maxLen < len)
					maxLen = len;
				if (x % 50 == 0)
					LENGTH++;
			}
			count++;
		}
	}

	private int getRand(int i) {
		return (int) (Math.random() * i);
	}

	private int getRand(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}
}
