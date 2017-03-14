package testing;

import java.net.SocketException;

import session.Session;

public class Server {
	
	public static void main(String[] args) {
		
		Session control = new Session();
		
		try {
			control.start();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}	
}
