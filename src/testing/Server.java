package testing;

import java.net.SocketException;

import networking.UDPListener;

public class Server {
	
	public static void main(String[] args) {
		try {
			Thread t = new Thread(new UDPListener("localserver"));
			t.start();
		} catch (SocketException e) {
			System.out.println("Failed to start server!");
			System.out.println(e.getMessage());
		}
	}
	
}
