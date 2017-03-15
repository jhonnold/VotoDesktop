package session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class Question {
	
	private Session currentSession;
	private int answer = (int)'A';
	private int imageID;
	private HashMap<Vote, ArrayList<Client>> answerSet = new HashMap<>();
	private HashMap<String, Vote> choices = new HashMap<>();
	public ArrayList<byte[]> questionImg = new ArrayList<byte[]>();
	
	/**
	 * Question constructor
	 * @param s - current session of this question
	 * @param img - image loaded with this question
	 * @param imageID - image ID for image param
	 */
	public Question(Session s, ArrayList<byte[]> img, int imageID) {

		questionImg = img;
		currentSession = s;
		this.imageID = imageID;
		choices.put("A", new Vote(1));
		choices.put("B", new Vote(2));
		choices.put("C", new Vote(3));
		choices.put("D", new Vote(4));
		choices.put("E", new Vote(5));
		
	}
	
	/**
	 * Sets the correct answer for current question
	 * @param ans - the correct answer
	 */
	public void setAnswer(int ans) { answer = ans; }
	
	/**
	 * Returns the image ID for current question
	 * @return the image ID
	 */
	public int imageID() { return imageID; }
	
	/**
	 * Returns the size of the current questions image
	 * @return current image size
	 */
	public int imageSize() { return questionImg.size(); }
	
	/**
	 * Adds a vote from a client to the current question 
	 * @param clientID - ID of the client sending the vote
	 * @param clientVote - the actual vote sent by the client
	 */
	public void addVote(String clientID, String clientVote) {

		// Get the clients most recent vote
		Client c = currentSession.getClient(clientID);
		Vote lastVote = c.getLastVote();
		
		// If no answer has been received for this client, remove them
		if (lastVote != null) {
			answerSet.get(lastVote).remove(c);
		}
		
		// Get the clients vote from the optional choices
		Vote v = choices.get(clientVote);
		
		// Add the vote to the client and set it as their most recent vote
		answerSet.get(v).add(c);
		c.setLastVote(v, lastVote);
	}
	
	/**
	 * Ends the current question and sets each client's last vote to
	 * be their final recorded vote
	 */
	public void endQuestion() {
		
		// Set the last vote of every client to their final vote
		for (Vote v : answerSet.keySet()) {
			
			for (Client c : answerSet.get(v)) {
				
				c.setLastVote(v, null);
			}
		}
	}
	
	/**
	 * Returns a byte array containing the image for the current question
	 * @param packetNum - the packet number for the desired image
	 * @return image byte array
	 * @throws IllegalArgumentException
	 */
	public byte[] getImagePacket(int packetNum) throws IllegalArgumentException {
		
		if (packetNum < 1 || packetNum >= questionImg.size() + 1) {

			throw new IllegalArgumentException("Packet number is invalid for this image");
		}
		
		return questionImg.get(packetNum - 1);
	}
	
}