package session;

import java.util.ArrayList;
import java.util.HashMap;


public class Question {
	
	private ArrayList<byte[]> image;
	private Session currentSession;
	private int answer;
	private HashMap<Integer, ArrayList<Client>> answerSet = new HashMap<>();
	
	/**
	 * Constructor for building question
	 * 
	 * @param img - an image (byte array) associated with a given question
	 * @param s - the current session the question is contained in
	 */
	public Question(ArrayList<byte[]> img, Session s) {
		image = img;
		currentSession = s;
	}
	
	/**
	 * Sets the answer for the given question
	 * 
	 * @param ans - answer for the given question
	 */
	public void setAnswer(int ans) {
		answer = ans;
	}
	
	/**
	 * Adds a client vote to the current question; keeps track of individual 
	 * client votes for updating when required
	 * 
	 * @param clientID
	 * @param clientVote
	 * @return
	 */
	public int addVote(String clientID, String clientVote) {
		
		Client c = currentSession.getClient(clientID);
		Integer lastVote = c.getLastVote();
		
		answerSet.get(lastVote).remove(c);
		
		Integer i = new Integer((int) (clientVote.toCharArray()[0]));
		
		answerSet.get(i).add(c);
		
		
		return 0;
	}
	
}
