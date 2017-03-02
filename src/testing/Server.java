package testing;

import java.net.SocketException;

import controller.Controller;

public class Server {
	
	public static void main(String[] args) {
		
		Controller control = new Controller();
		
		try {
			control.start();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}	
}
