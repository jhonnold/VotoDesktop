package networking;

import java.io.Closeable;
import java.net.DatagramPacket;
import java.net.SocketException;

import controller.Controller;
import gui.VotoDesktopFX;
import gui.VotoDesktopFX.Level;

/**
 * The NetworkHandler class watches a UDPSocket wrapper class that is designed
 * to listen on port 9876. With this it replies and receives packets from
 * clients
 */

public class NetworkHandler implements Runnable, Closeable {

	private UDPSocket socket;
	private Controller control;

	/**
	 * Creates a NetworkHandler linked up to its parent Controller.
	 * 
	 * @throws SocketException
	 *             - If something is already using port 9876
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
	 * Retrieves the byte array from the DatagramPacket before sending it onto
	 * the Controller parent who will parse it. It then gets the reply byte
	 * array from the parent and replies back to the client.
	 * 
	 * @param inFromClient
	 *            - The datagram packet received from client.
	 */
	public void onPacketReceived(DatagramPacket inFromClient) {
		byte[] data = inFromClient.getData();

		if (Level.ALL.lt(VotoDesktopFX.outputMode))
			System.out.println("I have received a packet containing: " + new String(data));

		try {
			byte[] replyData = control.parseNetworkCommand(data);
			reply(replyData, inFromClient);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();// replyError(kwargs, e.getMessage());
		}

	}

	/**
	 * Replies the byte array to the whoever sent the original DatagramPacket.
	 * 
	 * @param data
	 *            - The byte array to be sent out.
	 * @param in
	 *            - The datagram packet from the client that the packet is being
	 *            replied too.
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
	 * Starts the UDPSocket wrapper class
	 */
	@Override
	public void run() {
		Thread listening = new Thread(socket, "UDPSocket");
		listening.start();

		if (Level.LOW.lt(VotoDesktopFX.outputMode))
			System.out.println("Started listening for clients on port 9876!");
	}

	/**
	 * Closes the UDPSocket wrapper class
	 */
	@Override
	public void close() {
		socket.close();
	}

}
