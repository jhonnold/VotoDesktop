package networking;

import java.io.Closeable;
import java.net.DatagramPacket;
import java.net.SocketException;

import controller.Controller;

/**
 * 
 * @author zomby
 *
 * This class controls whats coming in and out of the UDPSocket
 * onPacketReceived, it passes it to the parser and finds
 * the proper command
 */

public class NetworkHandler implements Runnable, Closeable {
	
	private UDPSocket socket;
	private Controller control;
	
	/**
	 * Create the controller
	 * @throws SocketException - If something is already using port 9876
	 */
	public NetworkHandler(Controller control) throws SocketException {
		try {
			this.socket = new UDPSocket(this);
		} catch (SocketException e) {
			throw new SocketException(e.getMessage());
		}
		this.control = control;
	}
	
	/**
	 * Parses the DatagramPacket into a set of keyword arguments, passes them
	 * onto a command parser
	 * @param inFromClient - The datagram packet received from client
	 */
	public void onPacketReceived(DatagramPacket inFromClient) {
		byte[] data = inFromClient.getData();
		

		System.out.println("I have received a packet containing: " + new String(data));
		try {	
			reply(control.parseNetworkCommand(data), inFromClient);		
		} catch (IllegalArgumentException e) {
			e.printStackTrace();//replyError(kwargs, e.getMessage());
		}
		
	}
	
	/**
	 * Replies back to whoever sent the packet
	 */
	public void reply(byte[] data, DatagramPacket in) {
		DatagramPacket outToClient = new DatagramPacket(data, data.length, in.getAddress(), in.getPort());
		socket.send(outToClient);
	}
	
	/**
	 * Start the socket
	 */
	@Override
	public void run() {	
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

