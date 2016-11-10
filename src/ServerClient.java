import client.Frame;
import source.Server;


public class ServerClient {

	public static void main(String[] args) {
		int port = Server.DEFAULT_PORT;
        if (args.length == 2)
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                System.out.println("NaN" + args[1]);
            }
        new Server(port);
        new Frame();
	}

}
