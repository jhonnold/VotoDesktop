package testing;

import java.net.SocketException;

import controller.Controller;

public class Server {
	
	public static void main(String[] args) {
		
		Controller control = null;
		
		try {
			control = new Controller();
		} catch (Exception e) {
			System.exit(1);
		}
		
		new Thread(control).start();
	}	
}
