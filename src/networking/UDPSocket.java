package networking;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import gui.VotoDesktopFX.Level;

/**
 * This class opens a Datagram socket on a defined port and then both send and
 * receive
 */

public class UDPSocket implements Runnable, Closeable {

	private final int PORT = 9876;

	private NetworkHandler listener;
	private DatagramSocket socket;
	private volatile boolean isListening = false;

	/**
	 * Creates a UDPSocket that passes received packets up to the given Network
	 * Handler
	 * 
	 * @throws SocketException
	 *             - If the socket 9876 is in use.
	 */
	public UDPSocket(NetworkHandler l) throws SocketException {

		this.listener = l;

		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			throw new SocketException("Could not create socket on 9876.\nIs something else using it?");
		}
	}

	/**
	 * Starts listening for packets on port 9876. When a packet is received is
	 * passed up to the Network Handler where it is dealt with.
	 */
	@Override
	public void run() {

		isListening = true;

		try {
			socket.setSoTimeout(1000);
		} catch (SocketException e) {
			System.out.println("Couldn't set timeout");
		}

		while (isListening) {

			byte[] buffer = new byte[1024];
			DatagramPacket inFromClient = new DatagramPacket(buffer, buffer.length);

			try {
				socket.receive(inFromClient);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				System.out.println("Socket probably closed while blocking with receive");
			}

			listener.onPacketReceived(inFromClient);
		}
	}

	/**
	 * Send DatagramPacket to client, will continue to send until successful (1
	 * second timeout).
	 * 
	 * @param outToClient
	 *            - DatagramPacket to be sent
	 */
	public void send(DatagramPacket outToClient) {

		boolean hasSent = false;

		while (!hasSent) {
			try {
				socket.send(outToClient);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				System.out.println("IO issue, socket probably closed while trying to send");
			}
			hasSent = true;
		}

	}

	/**
	 * Wait a second for the isListening to take affect then close it cause the
	 * loop in run will have stopped This is not necessary but guarantees no
	 * error
	 */
	@Override
	public void close() {
		isListening = false;
		try {
			Thread.sleep(1000);
			socket.close();
		} catch (InterruptedException e) {
			System.out.println("Couldn't wait entire second!");
		}
	}

	/**
	 * Is the socket still listening
	 * 
	 * @return If the socket is still listening or not
	 */
	public boolean isListening() {
		return isListening;
	}

}
