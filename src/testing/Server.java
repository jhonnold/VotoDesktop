package testing;

import java.net.SocketException;

import networking.UDPListener;
import session.Session;

public class Server {
	
	public static void main(String[] args) {
		
		UDPListener listener = null;
		
		try {
			listener = new UDPListener("localserver");
		} catch (SocketException e) {
			System.out.println("Failed to start server!");
			System.out.println(e.getMessage());
		}
		
		Thread t = new Thread(listener, "Listener_Thread");
		t.start();
		
		Session s = new Session();
		listener.setCurrentSession(s);
		
		s.askQuestion("What is a+b?");
	}	
}
