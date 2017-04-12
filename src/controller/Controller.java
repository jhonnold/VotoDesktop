package controller;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import java.util.Arrays;

import gui.VotoDesktopFX;
import gui.VotoDesktopFX.Level;
import networking.NetworkHandler;
import session.Session;

public class Controller {

	private NetworkHandler network;
	private Session session;

	private boolean running = false;

	/**
	 * Creates a new Controller linked to the specified Session
	 * 
	 * @param session
	 *            - The session to which this controller will be passing
	 *            commands into
	 */
	public Controller(Session session) {
		this.session = session;
	}

	/**
	 * Starts a new NetworkHandler to start listening for incoming Clients. Does
	 * this on a new thread and specifies the Controller as running.
	 * 
	 * @throws SocketException
	 *             - If the port 9876 is already in use
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
	 * Stops the NetworkHandler from listening to client packets.
	 * 
	 * @throws IllegalArgumentException
	 *             - If the controller is not running
	 */
	public void stop() throws IllegalArgumentException {
		if (!running) {
			throw new IllegalArgumentException("Server is not currently active!");
		}

		network.close();
	}

	/**
	 * CLIENT COMMAND - A client has requested a handshake with the server and
	 * is expecting a reply from the server. The server will confirm the byte
	 * array is valid, confirm the ID is valid, and then add the new ID to the
	 * session. It then replies with the format of 'R' for handshake response, a
	 * byte for the length of the session ID, and then the session ID.
	 * 
	 * @param inFromClient
	 *            - {'R' (1), IDlength (1), ID (x)}
	 * @return - The returning packet as {'R' (1), IDLength (1), ID (x)}
	 */
	protected byte[] handshakeRequest(byte[] inFromClient, String IP) {
		if (inFromClient.length <= 1) {
			return null;
		}

		// Retrive string ID
		String ID = getDynamicData(inFromClient, 1);

		// If empty ID, ignore it
		if (ID.trim().equals("")) {
			return null;
		}

		if (Level.LOW.lt(VotoDesktopFX.outputMode)) {
			System.out.println("Parsed as a handshake from: " + ID);
		}

		if (!session.addClient(ID, IP)) {
			if (Level.LOW.lt(VotoDesktopFX.outputMode)) {
				System.out.println("This user has already connected!");
			}
			return null;
		}

		// Create the returning packet
		int l = session.ID.length();
		byte[] returnPacket = { 'R', (byte) l };
		return append(returnPacket, session.ID);
	}

	/**
	 * CLIENT COMMAND - A client has sent a vote packet to be parsed by the
	 * server. First it confirms it as a valid packet, then gets the ID and Vote
	 * Number. It then attempts to add the vote. If the new voteNum is less than
	 * a previous one, it will reject it. Prints the status of the vote and then
	 * returns a packet 'V' for Vote, 'R' for Response, and voteNum for the
	 * accepted Vote Number.
	 * 
	 * @param inFromClient
	 *            {'V' (1), IDlength (1), ID (x), voteNumber (1), Votelength
	 *            (1), Vote (x)}
	 * @return - The returning packet as {'V' (1), 'R' (1), voteNum (1)}
	 */
	protected byte[] vote(byte[] inFromClient) {
		if (Level.MED.lt(VotoDesktopFX.outputMode)) {
			System.out.println("Parsed as vote!");
		}

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

		if (Level.MED.lt(VotoDesktopFX.outputMode)) {
			System.out.println((voteSuccess ? "Vote Accepted" : "Vote Ignored"));
		}

		byte[] returnPacket = { 'V', 'R', (byte) voteNumber };
		return returnPacket;
	}

	/**
	 * CLIENT COMMAND - A Client is pinging for new media if there is any.
	 * Checks to make sure there is an image, then replies with 'M' for Media,
	 * 'P' for Ping, the imageID, the packet count, and then the image size.
	 * 
	 * @param inFromClient
	 *            {'M' (1), 'P' (1)}
	 * @return - The return packet as {'M' (1), 'P' (1), imgID (1), packetCount
	 *         (4), imageSize (4)}
	 */
	protected byte[] mediaPing(byte[] inFromClient) {

		// Dont reply to pings when no image is loaded
		if (!session.hasImage()) {
			return null;
		}

		byte[] returnPacket = { 'M', 'P', (byte) session.getCurrentImageID() };
		returnPacket = append(returnPacket,
				ByteBuffer.allocate(4).putInt(session.getCurrentImagePacketCount()).array());
		returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(session.getCurrentImageSize()).array());

		return returnPacket;
	}

	/**
	 * CLIENT COMMAND - A client is requesting a certain packet of the loaded
	 * media in the linked session. It gets the image ID they want and then gets
	 * the packet they want. It saves the payload the byte array along with the
	 * size of the payload before shipping it out. It starts with 'M' for Media
	 * and 'R' for response. It fails if they request a packet outside of the
	 * range.
	 * 
	 * @param inFromClient
	 *            {'M' (1), 'R' (1), imageID (1), packet# (1)}
	 * @return - The return packet as {'M' (1), 'R' (1), imageID (1),
	 *         packetNumber (4), payloadLength (4), payload (x)}
	 */
	protected byte[] mediaRequest(byte[] inFromClient) {
		int cursor = 2;
		int imageID = inFromClient[cursor++];
		int packetNumber = ByteBuffer.wrap(inFromClient, cursor, 4).getInt();
		
		if (Level.HIGH.lt(VotoDesktopFX.outputMode))
			System.out.println("Packet requested: " + packetNumber);

		try {
			byte[] payload = session.getImagePacket(imageID, packetNumber);
			byte[] returnPacket = { (byte) 'M', (byte) 'R', (byte) imageID };
			returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(packetNumber).array());
			returnPacket = append(returnPacket, ByteBuffer.allocate(4).putInt(payload.length).array());
			return append(returnPacket, payload);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * CLIENT COMMAND CONTROL POINT - This method is called directly from the
	 * NetworkHandler. It takes in the byte array and then figures out what the
	 * client was doing whether it was Handshaking (R), Voting (V), Media
	 * Pinging (MP), or Media Requesting (MR). It then replies with the proper
	 * byte array from each of the called methods.
	 * 
	 * @param data
	 *            - the byte array coming from the client.
	 * @return - the byte array to be returned based on the initial command
	 */
	public byte[] parseNetworkCommand(DatagramPacket inFromClient) {
		
		byte[] data = inFromClient.getData();
		byte[] returnPacket = null;

		char c = (char) data[0];

		if (c != 'R' || c != 'V' || c != 'M') {
			returnPacket = null;
		}

		switch (c) {
		case 'R':
			returnPacket = handshakeRequest(data, inFromClient.getAddress().getHostAddress());
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
	 * A private method that appends a String to the end of a byte array. It
	 * makes a new byte array of full length, copies the first to the start,
	 * then adds the each char, before returning.
	 * 
	 * @param data
	 *            - First byte array
	 * @param addition
	 *            - String to be added
	 * @return - A single byte array with the initial byte array in the lead,
	 *         the string as characters following.
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
	 * A private method that appends a byte array to the end of another byte
	 * array. Does so by making a new array of full size, copies the first array
	 * in one at a time. Follows with the second. Returns the combined array.
	 * 
	 * @param data
	 *            - first byte array
	 * @param addition
	 *            - second byte array
	 * @return - A single byte array with the first array leading, the second
	 *         follows.
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
	 * Retrieves a given data from byte array where the start index is the size
	 * of the string to be received
	 * 
	 * @param data
	 *            - the byte[] array containing the information
	 * @param start
	 *            - the index with the allocated size, start + 1 is where the
	 *            string begins
	 * @return - The String of retrieved data.
	 */
	private String getDynamicData(byte[] data, int start) {
		int length = data[start++];
		return new String(Arrays.copyOfRange(data, start, start + length));
	}

}
