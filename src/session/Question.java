package session;

import java.util.ArrayList;
import java.util.HashMap;


public class Question {
	
	private ArrayList<byte[]> image;
	private Session currentSession;
	private int answer = (int)'A';
	private int imageID;
	
	private HashMap<Integer, ArrayList<Client>> answerSet = new HashMap<>();
	
	public Question(Session s, ArrayList<byte[]> img, int imageID) {
		image = img;
		currentSession = s;
		this.imageID = imageID;
	}
	
	public void setAnswer(int ans) { answer = ans; }
	public int imageID() { return imageID; }
	public int imageSize() { return image.size(); }
	
	public void addVote(String clientID, String clientVote) {
		Client c = currentSession.getClient(clientID);
		Integer lastVote = c.getLastVote();
		
		answerSet.get(lastVote).remove(c);
		
		Integer i = new Integer((int) (clientVote.toCharArray()[0]));
		
		answerSet.get(i).add(c);
		c.setLastVote(i);
	}
	
	public byte[] getImagePacket(int packetNum) throws IllegalArgumentException {
		
		if (packetNum < 0 || packetNum >= image.size()) {
			throw new IllegalArgumentException("Packet number is invalid for this image");
		}
		
		return image.get(packetNum);
	}
	
}
