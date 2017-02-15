package testing;

import networking.UDPListener;

public class Server {
	
	public static void main(String[] args) {
		Thread t = new Thread(new UDPListener("localserver"));
		t.start();
	}
	
}
