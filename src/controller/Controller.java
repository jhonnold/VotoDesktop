package controller;

import java.util.logging.Logger;
import java.io.*;

import controller.tcp.*;
import controller.udp.*;

public class Controller implements TCPListener, UDPListener {
	
	private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());
	
	public static void main(String[] args) {
		
		Controller c = new Controller();
		c.start();
		
	}
	
	public void start() {
		
		String server_id = "";
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Input server_id: ");
			server_id = br.readLine();		
		} catch (Exception e) {
			LOGGER.severe("Error in reading server id");
		}
		
		
		Thread discoveryThread = new Thread(new UDPDiscovery(this, server_id));
		discoveryThread.setName("UDP Discovery Thread");
		
		LOGGER.info("Starting UDP Discovery Thread");
		discoveryThread.start();
	}
	
	
}
