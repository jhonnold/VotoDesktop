package testing;

import java.net.SocketException;

import controller.Controller;

public class Server {
	
	public static void main(String[] args) {
		
		Controller control = new Controller();
		
		try {
			control.start();
			Thread.sleep(10000);
			control.stop();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		
	}	
}
