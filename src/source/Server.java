package source;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {

	public static final int DEFAULT_PORT = 14000;
	private int port;
	private boolean stop;
	private Thread myThread;
	private Session session;
	
	public Server(int port) {
		this.port = port;
        session = new Session();
		myThread = new Thread(this);
		myThread.start();
	}
	
	@Override
	public void run() {
		try {
			echo("Trying to bind to port " + port);
			ServerSocket ss = new ServerSocket(port);
			echo("Successfully bound to port!");
			while(!stop) {
				echo("Listeneing for a connection...");
				doSomething(ss.accept());
			}
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		stop = true;
		myThread.interrupt();
	}

	public void doSomething(final Socket s) throws ClassNotFoundException {
		echo("New connection from " + s.getInetAddress());
		session.addConnection(s);	
	}	

	private static void echo(String s) {
		System.out.println("source.Server > " + s);
	}
}
