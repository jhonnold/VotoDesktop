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
	
	/**
	 * Constructor with the session this controller talks to
	 * @param session - the session that the controller passes commands into
	 */
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
		if (inFromClient.length <= 1) {
			return null;
		}
		
		// Retrive string ID
		String ID = getDynamicData(inFromClient, 1);
		
		// If empty ID, ignore it
		if (ID.trim().equals("")) {
			return null;
		}
		
		System.out.println("Parsed as a handshake from: "  + ID);
		if (!session.addClient(ID)) {
			System.out.println("This user has already connected!");
		}
		
		int l = session.ID.length();
		byte[] returnPacket = {'R', (byte) l};
		return append(returnPacket, session.ID);
	}

	/**
	 * CLIENT COMMAND - vote
	 * @param inFromClient {'V' (1), IDlength (1), ID (x), voteNumber (1), Votelength (1), Vote (x)}
	 * @return - the byte array to be returned
	 */
	protected byte[] vote(byte[] inFromClient) {
		System.out.println("Parsed as vote! " + new String(inFromClient));
		
		// invalid packet
		if (inFromClient.length <= 1) {
			return null;
		}
		
		int cursor = 1;		
		// Retrieve the ID of the client
		String ID = getDynamicData(inFromClient, cursor++);
		
		cursor += ID.length();
		
		// Get the number of the vote
		int voteNumber = (int) inFromClient[cursor++];
		
		String vote = getDynamicData(inFromClient, cursor++);
		
		// Apply the vote
		boolean voteSuccess = session.addClientVote(ID, vote, voteNumber);
		
		System.out.println((voteSuccess ? "Vote Accepted" : "Vote Ignored"));
		
		byte[] returnPacket = {'V', 'R', (byte) voteNumber};
		return returnPacket;
	}

	/**
	 * CLIENT COMMAND - mediaPing
	 * @param inFromClient {'M' (1), 'P' (1)} 
	 * @return - the byte array to be returned
	 */
	protected byte[] mediaPing(byte[] inFromClient) {
		
		// Dont reply to pings when no image is loaded
		if (!session.hasImage()) {
			return null;
		}
		
		byte[] returnPacket = {'M', 'P', (byte) session.getCurrentImageID()};
		returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(session.getCurrentImagePacketCount()).array());
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
		int packetNumber = ByteBuffer.wrap(inFromClient, cursor, 4).getInt();
		
		System.out.println("Packet requested: " + packetNumber);
		
		try {
			byte[] payload = session.getImagePacket(imageID, packetNumber);
			byte[] returnPacket = {(byte)'M', (byte)'R', (byte) imageID};
			returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(packetNumber).array());
			returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(payload.length).array());
			return append(returnPacket, payload);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
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
			returnPacket = null;
		}

		switch (c) {
			case 'R':
				returnPacket = handshakeRequest(data);
				break;
			case 'V':
				returnPacket = vote(data);
				break;
			case 'M':
				if (data[1] == 'P') {
					returnPacket = mediaPing(data);
				} else if (data[1] == 'R') {
					returnPacket = mediaRequest(data);
				} else {
					returnPacket = null;
				}
				break;
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
	