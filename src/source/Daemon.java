package source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Daemon implements Runnable {

	private ObjectReceiver or;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	protected boolean connected, looping;
	private Thread myThread;

	public Daemon(ObjectReceiver or) {
		this.or = or;
	}

	public void connect(Socket s) throws IOException {
		out = new ObjectOutputStream(s.getOutputStream());
		in = new ObjectInputStream(s.getInputStream());
		connected = true;
	}

	public void start() {
		if (!connected)
			return;
		stop();
		looping = true;
		myThread = new Thread(this);
		myThread.start();
	}

	public void stop() {
		looping = false;
		try {
			myThread.interrupt();
		} catch (Exception e) {
		}
	}

	public void close() throws IOException {
		if (!connected)
			return;
		stop();
		in.close();
		out.close();
		connected = false;
	}

	@Override
	public void run() {
		try {
			while (looping) {
				try {
					or.receive(in.readObject());
				} catch (ClassNotFoundException e) {
					System.out.println("Daemon.run");
				}
			}
		} catch (IOException e) {
			try {
				close();
			} catch (IOException e1) {
			}
		}
	}

	public boolean isConnected() {
		return connected;
	}

	private Object getObject() throws ClassNotFoundException, IOException {
		if (!connected)
			return null;
		return in.readObject();
	}

	public Object readObject() throws ClassNotFoundException, IOException {
		if (looping)
			return null;
		return getObject();
	}

	public void send(Serializable o) throws IOException {
		if ((!connected) || (o == null))
			return;
		out.writeObject(o);
		out.flush();
	}

	public void reset() throws IOException {
		if (!connected)
			return;
		out.reset();
	}

}
