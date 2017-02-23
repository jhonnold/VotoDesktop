package testing;

import java.net.SocketException;

import controller.NetworkController;
import controller.SessionController;

public class Server {
	
	public static void main(String[] args) {
		
		NetworkController nc = null;
		SessionController sc;
		
		try {
			nc = new NetworkController();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		sc = new SessionController();
		
		nc.addSessionController(sc);
		sc.addNetworkController(nc);
		
		Thread t1 = new Thread(nc);
		t1.start();
	}	
}
