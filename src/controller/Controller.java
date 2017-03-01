package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import networking.NetworkHandler;
import session.Session;

public class Controller {

	private NetworkHandler network;
	private Session session = new Session();

	private boolean running = false;

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
	 * Loads an image into an arraylist of bytearray of 64KB each
	 * 
	 * @param filename
	 *            - The filename of the image to be loaded
	 * @return - An arraylist of 60KB byte arrays
	 * @throws IOException
	 *             - if the filename is invalid
	 */
	public ArrayList<byte[]> loadImage(String filename) throws IOException {

		int packetsize = 61440; // 60 KB

		ArrayList<byte[]> packetBytes = new ArrayList<byte[]>();
		byte[] bytearray;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			// Load the image, write to stream, flush to guarantee all written
			BufferedImage img = ImageIO.read(new File(filename));
			ImageIO.write(img, "jpg", baos);
			baos.flush();
		} catch (IOException e) {
			throw new IOException("Could not load specified file!");
		}

		// Grab the byte array
		bytearray = baos.toByteArray();

		// cut the full array into 60 KB by using copyOfRange
		for (int i = 0; i < bytearray.length; i += packetsize) {
			packetBytes.add(Arrays.copyOfRange(bytearray, i, Math.min(bytearray.length, i + packetsize)));
		}

		return packetBytes;

		/**
		 * To decode these packets back into an image: Combine all the packets
		 * into a single bytearray BufferedImage img = ImageIO.read(new
		 * ByteArrayInputStream(bytearray)); ImageIO.write(img, "jpg", new
		 * File(dirName, "snap.jpg"));
		 */
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
		byte[] returnPacket = {'V'};
		return returnPacket;
	}

	/**
	 * CLIENT COMMAND - mediaPing
	 * @param inFromClient {'M' (1), 'P' (1)} 
	 * @return - the byte array to be returned
	 */
	protected byte[] mediaPing(byte[] inFromClient) {
		
		byte[] returnPacket = {'M', 'P', (byte) session.getCurrentImageID(), (byte) session.getCurrentImagePacketCount()};
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

		if (c != 'R' && c != 'V' && c != 'M') {
			throw new IllegalArgumentException("This is not a recognized packet!");
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
					throw new IllegalArgumentException("This is not a recognized packet!");
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
