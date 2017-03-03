package session;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import controller.Controller;

public class Session {
	
	public String ID = "test server";
	private ArrayList<String> clientList = new ArrayList<String>();
	
	private Controller control = new Controller(this); 
	
	public void start() throws SocketException {
		try {
			control.start();
		} catch (SocketException e) {
			throw e;
		}
	}
	
	public void stop() throws IllegalArgumentException {
		try {
			control.stop();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	public void addClient(String client) {
		if (!clientList.contains(client)) {
			System.out.println("New client, added as: " + client);
			clientList.add(client);
		} else {
			System.out.println("Client already exists, not adding");
		}
	}
	
	public int getCurrentImageID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCurrentImagePacketCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isConnectedClient(String string) {
		// TODO Auto-generated method stub
		return true;
	}

	public byte[] getImagePacket(int imageID, int packetNumber) {
		// TODO Auto-generated method stub
		return null;
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
}
