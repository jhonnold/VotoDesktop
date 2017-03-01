package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import networking.NetworkHandler;
import session.Session;

public class Controller {
	
	
	private NetworkHandler network;
	private Session session = new Session();
	
	private boolean running = false;
	
	public Controller() {}
	
	/**
	 * Starts the network socket to start accepting
	 * packets from clients
	 * @throws SocketException - if the port 9876 is in use
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
	 * Stops the network socket from accepting packets
	 * from clients
	 * @throws IllegalArgumentException - if nothing is running
	 */
	public void stop() throws IllegalArgumentException {
		if (!running) {
			throw new IllegalArgumentException("Server is not currently active!");
		}
		
		network.close();
	}
	
	
	/**
	 * Loads an image into an arraylist of bytearray of 64KB each
	 * @param filename - The filename of the image to be loaded
	 * @return - An arraylist of 60KB byte arrays
	 * @throws IOException - if the filename is invalid
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
		 * To decode these packets back into an image:
		 * Combine all the packets into a single bytearray
		 * BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytearray));
		 * ImageIO.write(img, "jpg", new File(dirName, "snap.jpg"));
		 */
	}
	
	/**
	 * RESERVED FOR NETWORK PACKETS
	 * Handshake request received 
	 * @param kwargs - [1-CLIENTIP 2-CLIENTPORT 3-CLIENTID]
	 * @replies - handshakeRequest_[sessionID]
	 */
	public void handshakeRequest(ArrayList<String> kwargs) {
		System.out.println("Parsed as a handshake");
		
		if (kwargs.size() <= 2) {
			System.out.println("Not formatted properly, replying with error");
			network.replyError(kwargs, "ERROR handshakeRequest_[id]");
		} else if (kwargs.size() == 3) {
			session.addClient(kwargs.get(1));
			kwargs.add("" + session.ID);
			network.reply(kwargs);
		} else {
			session.addClient(kwargs.size() == 3 ? kwargs.get(1) : kwargs.get(3));
			kwargs.set(3, "" + session.ID);
			network.reply(kwargs);
		}
	}
	
	/**
	 * RESERVED FOR NETWORK PACKETS
	 * Vote was received
	 * @param kwargs - [1-CLIENTIP 2-CLIENTPORT 3-CLIENTID 4-VOTE]
	 * @replies - vote_[sessionID]_[voteString]
	 */
	public void vote(ArrayList<String> kwargs) {
		
		//TODO parse and save vote to session
		
		kwargs.set(3, "" + session.ID);
		network.reply(kwargs);
	}
	
	/**
	 * RESERVED FOR NETWORK PACKETS
	 * client is requesting information on current media
	 * @param kwargs - [1-CLIENTIP 2-CLIENTPORT]
	 * @replies - mediaPing_[imageID]_[packetCount]
	 */
	public void mediaPing(ArrayList<String> kwargs) {
		
		if (kwargs.size() > 3) {
			network.replyError(kwargs, "ERROR mediaPing should have nothing following it");
		}
		
		if (!session.isConnectedClient(kwargs.get(1))) {
			network.replyError(kwargs, "ERROR handshakeRequest before any other commands!");
		}
		
		int imageID = session.getCurrentImageID();
		int packetCount = session.getCurrentImagePacketCount();
		
		kwargs.add("" + imageID);
		kwargs.add("" + packetCount);
		
	}
	
	/**
	 * Parses an incoming network command and invokes
	 * @param kwargs - The keyword arguments passed in from the client
	 * @throws NoSuchMethodException - if this method doesn't exist
	 */
	public void parseNetworkCommand(ArrayList<String> kwargs) throws NoSuchMethodException {
		
		try {
			Method method = this.getClass().getMethod(kwargs.get(0), ArrayList.class); 
			method.invoke(this, kwargs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("Gibberish");
		}
		
	}
	
}
