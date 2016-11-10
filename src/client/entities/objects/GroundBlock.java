package client.entities.objects;

import client.entities.EntityType;
import client.entities.StageEntity;
import client.interfaces.Solid;

import java.awt.*;

/**
 * Created by zeriksen on 7/2/2014.
 */
public class GroundBlock extends StageEntity implements Solid {
	private int length;

	public GroundBlock(double x, double y, int length) {
		super(x, y, length * 10, 10, EntityType.ground);
		this.length = length;
	}

	@Override
	public void paint(Graphics g) {
		paint(g, 0, 0);
	}
	
	@Override
	public void paint(Graphics g, int dx, int dy) {
		g.setColor(Color.white);
		for(int i = 0; i < length; i++) {
			g.drawRect((int)x + (i*10) + dx, (int)y + dy, 10, 10);
		}
	}

	@Override
	public void paint(Graphics g, int dx, int dy,Color c) {
//		for (int i = (int) x; i < (length) + x; i += 10) {
		g.setColor(c);
		for(int i = 0; i < length; i++) {
			g.drawRect((int)x + (i*10) + dx, (int)y + dy, 10, 10);
		}
		g.setColor(Color.white);
	}
	
	public void paintHighscore(Graphics g, int dx, int dy, Color c){
		paint(g,dx,dy,c);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, length * 10, 10);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroundBlock other = (GroundBlock) obj;
		if (getBounds() != other.getBounds())
			return false;
		return true;
	}
	
	
}
