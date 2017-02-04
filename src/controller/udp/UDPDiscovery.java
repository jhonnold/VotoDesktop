package controller.udp;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPDiscovery implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(UDPDiscovery.class.getName());
	
	MulticastSocket discoverySocket = null;
	UDPListener controller;
	
	private final int PORT = 9876;
	private final String ID;
	private InetAddress GROUP; 
	
	public UDPDiscovery(UDPListener controller, String id) {
		
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("Creating UDPDiscovery on port 9876");
		
		this.controller = controller;
		this.ID = id;
		
		try {
			GROUP = InetAddress.getByName("224.0.0.3");
		} catch (UnknownHostException e) {
			LOGGER.severe("The multicast group: 224.0.0.3 is invalid on this router");
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		try {
			
			discoverySocket = new MulticastSocket(PORT);
			discoverySocket.joinGroup(GROUP);
			
			LOGGER.info("Opened socket 9876 and joined group 224.0.0.3");
			
			while (true) {
				LOGGER.info("Waiting for handshake request");
				
				byte[] buffer = new byte[1024];
				DatagramPacket inFromClient = new DatagramPacket(buffer, buffer.length);
				discoverySocket.receive(inFromClient);
				
				LOGGER.info("Packet received from: " + inFromClient.getAddress().getHostAddress());
				
				String contents = new String(inFromClient.getData()).trim();
				
				if (contents.equals("VOTO_HANDSHAKE_REQUEST")) {
					LOGGER.info("Packet was a handshake request!");
					LOGGER.info("Replying...");
					
					byte[] send = ("VOTO_HANDSHAKE_RESPONSE_" + ID).getBytes();
					
					DatagramPacket outToClient = new DatagramPacket(send, send.length, inFromClient.getAddress(), inFromClient.getPort());
					discoverySocket.send(outToClient);
				} else {
					LOGGER.info("Packet was not a handshake request");
				}
			}
			
		} catch (Exception e) {
			LOGGER.severe(e.toString());
		}

	}

}
