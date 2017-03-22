package session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class Question {
	
	private Session currentSession;
	private Vote answer;
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
		
		answer = choices.get("A");
		
	}
	
	/**
	 * Sets the correct answer for current question
	 * @param ans - String for the correct answer
	 */
	public void setAnswer(String ans) {
		answer = choices.get(ans);
	}
	
	/**
	 * Returns the correct answer for this question
	 * @return the Vote corresponding to the correct answer
	 */
	public Vote getAnswer() { return answer; }
	
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
	public boolean addVote(Client client, String clientVote, int voteNum) {
		
		if (voteNum > client.voteNum) {
			client.voteNum = voteNum;
			Vote v = choices.get(clientVote);
			
			if (v == null) {
				System.out.println("Vote is invalid!");
				return false;
			}
			
			ArrayList<Client> voters = answerSet.get(v);
			
			Vote lastVote = client.getLastVote();
			
			if (lastVote != null) {
				answerSet.get(lastVote).remove(client);
			}
			
			if (voters == null) {
				voters = new ArrayList<Client>();
			}
			
			voters.add(client);
			client.setLastVote(v);
			
			System.out.println("Added vote for " + client.getID() + ": "+ clientVote);
			
			return true;
		} else {
			System.out.println("Votenum is less than previous vote: " + voteNum + " " + client.voteNum);
			return false;
		}
		
		
		
		
	}
	
	/**
	 * Ends the current question and sets each client's last vote to
	 * be their final recorded vote
	 */
	public void endQuestion() {
		// Set the last vote of every client to their final vote
		for (Vote v : answerSet.keySet()) {
			
			for (Client c : answerSet.get(v)) {
				
				c.setAnswerVote(this, v);
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
	
	/**
	 * Returns the mapping of clients to their votes for the current question
	 * @return HashMap of votes listing the clients who selected it
	 */
	public HashMap<Vote, ArrayList<Client>> getAnswerSet() {
		return answerSet;
	}
	
}