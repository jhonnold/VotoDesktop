package controller;

import java.net.SocketException;
import java.nio.ByteBuffer;

import java.util.Arrays;
import networking.NetworkHandler;
import session.Session;

public class Controller {

	private NetworkHandler network;
	private Session session;
	
	private boolean running = false;
	
	public Controller(Session session) {
		this.session = session;
	}
	
	/**
	 * Starts the network socket to start accepting packets from clients
	 * 
	 * @throws SocketException
	 *             - if the port 9876 is in use
	 */
	public void start() throws SocketException {
		try {
			network = new NetworkHandler(this);
		} catch (SocketException e) {
			throw new SocketException("Failed to start the listening socket.");
		}

		new Thread(network).start();
		running = true;
	}

	/**
	 * Stops the network socket from accepting packets from clients
	 * 
	 * @throws IllegalArgumentException
	 *             - if nothing is running
	 */
	public void stop() throws IllegalArgumentException {
		if (!running) {
			throw new IllegalArgumentException("Server is not currently active!");
		}

		network.close();
	}
	
	/**
	 * CLIENT COMMAND - handshakeRequest
	 * @param inFromClient {'R' (1), IDlength (1), ID (x)}
	 * @return - the byte array to be returned
	 */
	protected byte[] handshakeRequest(byte[] inFromClient) {
		String ID = getDynamicData(inFromClient, 1);
		System.out.println("Parsed as a handshake from: "  + ID);
		session.addClient(ID);
		
		int l = session.ID.length();
		byte[] returnPacket = {'R', (byte) l};
		return append(returnPacket, session.ID);
	}

	/**
	 * CLIENT COMMAND - vote
	 * @param inFromClient {'V' (1), IDlength (1), ID (x), voteNumber (1), Votelength (1), Vote (x)}
	 * @return - the byte array to be returned
	 */
	@SuppressWarnings("unused")
	protected byte[] vote(byte[] inFromClient) {
		int cursor = 1;		
		String ID = getDynamicData(inFromClient, cursor);
		cursor += ID.length();
		
		int voteNumber = inFromClient[cursor++];
		
		String vote = getDynamicData(inFromClient, cursor);
		
		//do something with the vote
		byte[] returnPacket = {'V', 'R', (byte) voteNumber};
		return returnPacket;
	}

	/**
	 * CLIENT COMMAND - mediaPing
	 * @param inFromClient {'M' (1), 'P' (1)} 
	 * @return - the byte array to be returned
	 */
	protected byte[] mediaPing(byte[] inFromClient) {
		
		byte[] returnPacket = {'M', 'P', (byte) session.getCurrentImageID(), (byte) session.getCurrentImagePacketCount()};
		returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(session.getCurrentImageSize()).array());
		
		return returnPacket;
	}
	
	/**
	 * CLIENT COMMAND - mediaRequest
	 * @param inFromClient {'M' (1), 'R' (1), imageID (1), packet# (1)}
	 * @return - the byte array to be returned
	 */
	protected byte[] mediaRequest(byte[] inFromClient) {
		int cursor = 2;
		int imageID = inFromClient[cursor++];
		int packetNumber = inFromClient[cursor++];
		
		byte[] payload = session.getImagePacket(imageID, packetNumber);
		
		byte[] returnPacket = {'M', 'R', (byte) imageID, (byte) packetNumber};
		
		returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(payload.length).array());
		return append(returnPacket, payload);
	}
	
	/**
	 * CLIENT COMMAND CONTROL POINT - handles all incoming client commands
	 * @param data the byte array containing the command params
	 * @return - the byte array to be returned based on the initial command
	 * @throws IllegalArgumentException - if the command given is invalid
	 */
	public byte[] parseNetworkCommand(byte[] data) throws IllegalArgumentException {

		byte[] returnPacket = null;

		char c = (char) data[0];

		if (c != 'R' || c != 'V' || c != 'M') {
			//throw new IllegalArgumentException("This is not a recognized packet!");
		}

		switch (c) {
			case 'R':
				returnPacket = handshakeRequest(data);
			case 'V':
				returnPacket = vote(data);
			case 'M':
				if (data[1] == 'P') {
					returnPacket = mediaPing(data);
				} else if (data[1] == 'R') {
					returnPacket = mediaRequest(data);
				} else {
					//throw new IllegalArgumentException("This is not a recognized packet!");
				}
		}
		
		return returnPacket;
	}
	
	/**
	 * @param data - first byte array
	 * @param addition - string to be added
	 * @return - a single byte array connected
	 */
	private byte[] append(byte[] data, String addition) {
		
		byte[] temp = new byte[data.length + addition.length()];
		char[] chars = addition.toCharArray();
		
		for (int i = 0; i < data.length; i++) {
			temp[i] = data[i];
		}
		
		for (int i = 0; i < chars.length; i++) {
			temp[i + data.length] = (byte) chars[i];
		}
		
		return temp;
	}
	
	/**
	 * @param data - first byte array
	 * @param addition - second byte array
	 * @return - a single byte array connected
	 */
	private byte[] append(byte[] data, byte[] addition) {
		byte[] temp = new byte[data.length + addition.length];
		
		for (int i = 0; i < data.length; i++) {
			temp[i] = data[i];
		}
		
		for (int i = 0; i < addition.length; i++) {
			temp[i + data.length] = addition[i];
		}
		
		return temp;
	}
	
	/**
	 * Retrieves a given data from byte array where the start index is the size of the string
	 * to be received
	 * @param data - the byte[] array containing the information
	 * @param start - the index with the allocated size, start + 1 is where the string begins
	 * @return - the retrieved data
	 */
	private String getDynamicData(byte[] data, int start) {
		int length = data[start++];
		return new String(Arrays.copyOfRange(data, start, start + length));
	}
	
}
