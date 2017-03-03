package session;

import java.util.ArrayList;
import java.util.HashMap;


public class Question {
	
	private ArrayList<byte[]> image;
	private Session currentSession;
	private int answer;
	private HashMap<Integer, ArrayList<Client>> answerSet = new HashMap<>();
	
	public Question(ArrayList<byte[]> img, Session s) {
		image = img;
		currentSession = s;
	}
	
	
	public void setAnswer(int ans) {
		answer = ans;
	}
	
	public int addVote(String clientID, String clientVote) {
		
		Client c = currentSession.getClient(clientID);
		Integer lastVote = c.getLastVote();
		
		answerSet.get(lastVote).remove(c);
		
		Integer i = new Integer((int) (clientVote.toCharArray()[0]));
		
		answerSet.get(i).add(c);
		
		
		return 0;
	}
	
}
