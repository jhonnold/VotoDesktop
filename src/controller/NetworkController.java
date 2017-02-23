package controller;

import java.io.Closeable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import networking.UDPSocket;
import utilities.CommandParser;

/**
 * 
 * @author zomby
 *
 * This class controls whats coming in and out of the UDPSocket
 * onPacketReceived, it passes it to the parser and finds
 * the proper command
 */

public class NetworkController implements Runnable, Closeable {
	
	private UDPSocket socket;
	private SessionController sessionController;
	
	/**
	 * Create the controller
	 * @throws SocketException - If something is already using port 9876
	 */
	public NetworkController() throws SocketException {
		try {
			this.socket = new UDPSocket(this);
		} catch (SocketException e) {
			throw new SocketException(e.getMessage());
		}
	}
	
	public void addSessionController(SessionController sc) {
		this.sessionController = sc;
	}
	
	/**
	 * Parses the DatagramPacket into a set of keyword arguments, passes them
	 * onto a command parser
	 * @param inFromClient - The datagram packet received from client
	 */
	public void onPacketReceived(DatagramPacket inFromClient) {
		String data = new String(inFromClient.getData()).trim();
		
		System.out.println("I have received a packet containing: " + data);
		
		//split the incoming message into arguments based on _
		//this will need to change in the future
		ArrayList<String> kwargs = new ArrayList<String>(Arrays.asList(data.split("_")));
		
		//put the port and ip address into the keyword arguments (index 1 and 2)
		kwargs.add(1, ""  + inFromClient.getPort());
		kwargs.add(1, inFromClient.getAddress().getHostAddress());
		try {	
			CommandParser.parseCommand(sessionController, kwargs);		
		} catch (NoSuchMethodException e) {
			replyError(kwargs, e.getMessage());
		}
		
	}
	
	/**
	 * Replies back to whoever sent the packet
	 * @param kwargs - [0-originalcommand 1-ip 2-port 3-message]
	 * 				 - originalcommand will become error if error
	 */
	public void reply(ArrayList<String> kwargs) {
		byte[] buffer = (kwargs.get(0) + "_" + kwargs.get(3)).getBytes();
		
		try {
			InetAddress address = InetAddress.getByName(kwargs.get(1));
			int port = Integer.parseInt(kwargs.get(2));
			DatagramPacket out = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(out);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Replied with: " + (new String(buffer)));
		
	}
	
	public void replyError(ArrayList<String> kwargs, String errormsg) {
		byte[] buffer = errormsg.getBytes();
		
		try {
			InetAddress address = InetAddress.getByName(kwargs.get(1));
			int port = Integer.parseInt(kwargs.get(2));
			DatagramPacket out = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Replied with: " + (new String(buffer)));
	}
	
	/**
	 * Start the socket
	 */
	@Override
	public void run() {
		System.out.println("Starting up socket...");
		Thread listening = new Thread(socket, "UDPSocket");
		listening.start();
		
		System.out.println("Started listening for clients on port 9876!");
	}
	
	/**
	 * Close the socket
	 */
	@Override
	public void close() {
		socket.close();
	}
	
}

