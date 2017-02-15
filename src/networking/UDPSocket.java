package networking;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * 
 * @author zomby
 *
 * This class is designed to open a Datagram socket on
 * a defined port and then both send and receive
 */

public class UDPSocket implements Runnable, Closeable {
	
	private final int PORT = 9876;
	
	private UDPListener listener;
	private DatagramSocket socket;
	private volatile boolean isListening = false;
	
	/**
	 * Create a new datagram socket, catch the error of
	 * something else using the port.
	 */
	public UDPSocket(UDPListener listner) throws SocketException {
		
		this.listener = listner;
		
		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			throw new SocketException("Could not create socket on 9876.\nIs something else using it?");
		}
		
	}
	
	/**
	 * Start listening and in an infite loop constantly receive
	 * packets, sending them up to the listner.
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
	 * Send DatagramPacket to client
	 * @param outToClient - Datagram packet to be sent
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
	 * Wait a second for the isListening to take affect
	 * then close it cause the loop in run will have stopped
	 * This is not necessary but guarantees no error
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
	
	
}
