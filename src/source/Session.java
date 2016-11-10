package source;

import client.entities.objects.PlayerBlock;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Vector;

public class Session implements ObjectReceiver, Runnable {

	protected Vector<Daemon> virtualClients;
	private World masterWorld;
	private byte counterId = 0;
	private boolean clientHasId;

	public Session() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String myIP = addr.getHostAddress();
			System.out.println(myIP);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e);
		}
		virtualClients = new Vector<Daemon>();
		masterWorld = new World();
		new Thread(this).start();
	}

	public void addConnection(Socket s) throws ClassNotFoundException {
		try {
			Daemon d = new Daemon(this);
			System.out.println("Session.addConnection.id: " + counterId);
			d.connect(s);
			System.out.println("Sending ping");
			d.send("p");
			if (d.readObject().equals("pp")) {
				System.out.println("Got pong starting");
				d.send(counterId);
			}
			masterWorld.players.put(counterId,
					new PlayerBlock(10, masterWorld.getStartY() - 10,
							createColor(counterId)));
			masterWorld.players.get(counterId).giveId(new Byte(counterId));
			counterId++;
			d.start();
			virtualClients.add(d);
			refresh();
		} catch (IOException e) {
			System.out.println("\tSession.addConnection");
			e.printStackTrace();
		}
	}

	public void refresh() {
		for (Daemon vc : virtualClients) {
			try {
				vc.reset();
			} catch (IOException e) {
			}
		}
	}

	private Color createColor(int i) {
		Color c;
		switch (i) {
		case 0:
			c = Color.red;
			break;
		case 1:
			c = Color.blue;
			break;
		case 2:
			c = Color.green;
			break;
		case 3:
			c = Color.orange;
			break;
		case 4:
			c = Color.magenta;
			break;
		case 5:
			c = Color.cyan;
			break;
		case 6:
			c = Color.pink;
			break;
		case 7:
			c = Color.yellow;
			break;
		case 8:
			c = Color.lightGray;
			break;
		default:
			Random rand = new Random();
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			return new Color(r, g, b);
		}
		return c;
	}

	@Override
	public synchronized void receive(Object o) throws IOException {
		if (o == null)
			return;
		String[] corn = o.toString().split(":");
		byte id = Byte.parseByte(corn[0]);
		char key = corn[1].charAt(0);
		if (o.toString().contains("j")) {
			masterWorld.players.get(id).jump();
			if (!masterWorld.players.get(id).isOnSurface()) {
				refresh();
			}
		} else if (o.toString().contains(":")) {
			masterWorld.players.get(id).sendKey(key + "");
		}
	}

	private void sync() {
		int index = 0;
		while (index < virtualClients.size()) {
			try {
				Daemon d = virtualClients.get(index);
				if (!d.isConnected()) {
					System.out.println("Removing: " + index);
					virtualClients.remove(index);
					masterWorld.removePlayer(Byte.parseByte(index + ""));
					refresh();
				} else {
					d.send(masterWorld);
					index++;
				}
			} catch (IOException e) {
			}
		}
	}

	@Override
	public void run() {
		System.out.println("run");
		while (true) {
			masterWorld.update(.016);
			sync();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				System.out.println("ummm");
			}
		}
	}
}
