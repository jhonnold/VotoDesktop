package testing;

import java.net.SocketException;

import controller.Controller;
import session.Session;

public class Server {
	
	public static void main(String[] args) {
		
		Session s = new Session();
		
		try {
			s.start();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}	
}
