package controller;

import java.util.logging.Logger;

import controller.tcp.*;
import controller.udp.*;

public class Controller implements TCPListener, UDPListener {
	
	private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());
	
	public static void main(String[] args) {
		
		Controller c = new Controller();
		c.start();
		
	}
	
	public void start() {
		
		Thread discoveryThread = new Thread(new UDPDiscovery(this));
		discoveryThread.setName("UDP Discovery Thread");
		
		LOGGER.info("Starting UDP Discovery Thread");
		discoveryThread.start();
	}
	
	
}
