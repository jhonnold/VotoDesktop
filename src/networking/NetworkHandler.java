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
			byte[] replyData = control.parseNetworkCommand(data);
			reply(replyData, inFromClient);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();//replyError(kwargs, e.getMessage());
		}
		
	}
	
	/**
	 * Replies the given byte array to the location of the datagram packet
	 * @param data - the byte array to be sent
	 * @param in - the datagram packet to have the byte array sent too
	 */
	public void reply(byte[] data, DatagramPacket in) {
			
		if (data != null) {
			DatagramPacket outToClient = new DatagramPacket(data, data.length, in.getAddress(), in.getPort());
			socket.send(outToClient);
		} else {
			return;
		}
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

