package testing;

import java.net.SocketException;

import session.Session;

public class Server {
	
	public static void main(String[] args) {
		Session s = new Session("test");
		
		try {
			s.start();
			s.setCurrentQuestion("testimage.jpg", "A");
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}	
}
