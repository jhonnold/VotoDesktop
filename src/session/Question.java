package session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class Question {
	
	private ArrayList<byte[]> image;
	private Session currentSession;
	private int answer = (int)'A';
	private int imageID;
	
	private HashMap<Vote, ArrayList<Client>> answerSet = new HashMap<>();
	private HashMap<String, Vote> choices = new HashMap<>();
	
	
	public Question(Session s, ArrayList<byte[]> img, int imageID) {
		image = img;
		currentSession = s;
		this.imageID = imageID;
		
		choices.put("A", new Vote(1));
		choices.put("B", new Vote(2));
		choices.put("C", new Vote(3));
		choices.put("D", new Vote(4));
		choices.put("E", new Vote(5));
	}
	
	public void setAnswer(int ans) { answer = ans; }
	public int imageID() { return imageID; }
	public int imageSize() { return image.size(); }
	
	public void addVote(String clientID, String clientVote) {
		Client c = currentSession.getClient(clientID);
		Vote lastVote = c.getLastVote();
		
		if (lastVote != null) {
			answerSet.get(lastVote).remove(c);
		}
		
		Vote v = choices.get(clientVote);
		
		answerSet.get(v).add(c);
		c.setLastVote(v, lastVote);
	}
	
	public byte[] getImagePacket(int packetNum) throws IllegalArgumentException {
		
		if (packetNum < 0 || packetNum >= image.size()) {
			throw new IllegalArgumentException("Packet number is invalid for this image");
		}
		
		return image.get(packetNum);
	}
	
}
