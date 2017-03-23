package session;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import controller.Controller;

public class Session {
	
	public final String ID;
	private ArrayList<Client> clientList = new ArrayList<Client>();
	
	private Controller control = new Controller(this); 
	private Question currentQuestion;
	
	private int imageID = 1;
	
	public Session(String ID) {
		this.ID = ID;
	}
	
	/**
	 * Starts a Voto session
	 * @throws SocketException
	 */
	public void start() throws SocketException {
		try {
			control.start();
		} catch (SocketException e) {
			throw e;
		}
	}
	
	/**
	 * Stops (or ends) the current Voto session
	 * @throws IllegalArgumentException
	 */
	public void stop() throws IllegalArgumentException {
		try {
			control.stop();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	/**
	 * the current Question
	 * @return the current quesion
	 */
	public Question getCurrentQuestion() {
		return currentQuestion;
	}
	
	/**
	 * Sets a new Question for the Session.
	 * Loads image via filename and sets the correct answer to be
	 * that of the inputted string.
	 * @param filename The image file to be loaded
	 * @param answer The answer string to be accepted 
	 * @return True if loads properly, False if image fails
	 */
	public boolean setCurrentQuestion(String filename, String answer) {
		
		if (currentQuestion != null) {
			currentQuestion.endQuestion();
			for (Client c : clientList) {
				c.setLastVote(null);
			}
			
		}
		
		try {
			ArrayList<byte[]> imageBytes = loadImage(filename);
			currentQuestion = new Question(imageBytes, imageID++);
			currentQuestion.setAnswer(answer);
			return true;
		} catch (IOException e) {
			System.out.println("Failed to load image!");
			return false;
		}
		
	}
	
	/**
	 * Adds a new client to the session's client list with their ID
	 * @param ID - the new client's ID
	 */
	public boolean addClient(String ID) {
		if (clientList.contains(new Client(ID))) {
			return false;
		}
		
		System.out.println("Added new client: " + ID);
		return clientList.add(new Client(ID));
	}
	
	/**
	 * Returns a client object based off of the client ID passed in
	 * @param clientID - ID of the desired client
	 * @return a client object
	 */
	public Client getClient(String clientID) {
		
		// Search the client list for the specified ID
		for (Client c : clientList) {
			if (c.getID().equals(clientID)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Adds a client vote to the current Question
	 * @param clientID The string of the clients ID
	 * @param vote The vote string
	 * @return false if not added or invalid, true if added
	 */
	public boolean addClientVote(String clientID, String vote, int voteNum) {
		
		Client c = getClient(clientID);
		if (c == null) {
			System.out.println("Client does not exist!");
			return false;
		}
		
		return currentQuestion.addVote(c, vote, voteNum);
		
	}
	
	
	/**
	 * Returns the ID for the image of the current question
	 * @return ID of current question image
	 */
	public int getCurrentImageID() {
		return currentQuestion.imageID();
	}

	/**
	 * Returns the number of packets for the current question image
	 * @return image packet count
	 */
	public int getCurrentImagePacketCount() {
		return currentQuestion.imageSize();
	}

	/**
	 * Returns the image packet for the current question of the session
	 * @param imageID - ID of the question image
	 * @param packetNumber - corresponding packet number
	 * @return byte array packet for the current image
	 * @throws IllegalArgumentException
	 */
	public byte[] getImagePacket(int imageID, int packetNumber) throws IllegalArgumentException {
		if (imageID != currentQuestion.imageID()) {
			throw new IllegalArgumentException("Cannot request this image at this time " + imageID);
		}
		
		return currentQuestion.getImagePacket(packetNumber);
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
		
		System.out.println("Loaded image completely");
		
		return packetBytes;

		/**
		 * To decode these packets back into an image: Combine all the packets
		 * into a single bytearray BufferedImage img = ImageIO.read(new
		 * ByteArrayInputStream(bytearray)); ImageIO.write(img, "jpg", new
		 * File(dirName, "snap.jpg"));
		 */
	}

	/**
	 * Returns the size (in bytes) of the current session image
	 * @return image size in bytes
	 */
	public int getCurrentImageSize() {
		int total = 0;
		
		for (byte[] b : currentQuestion.questionImg) {
			total += b.length;
		}
		
		return total;
	}
	
	/**
	 * If a current Question is loaded
	 * @return - True if loaded, false if not (null)
	 */
	public boolean hasImage() {
		return (currentQuestion != null);
	}
}
