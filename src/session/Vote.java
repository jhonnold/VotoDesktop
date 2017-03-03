package session;

import java.util.ArrayList;

public class Vote {

	private String ID;
	private String IP;
	private String vote;
	
	// Vote constructor (when provided ID)
	public Vote(String clientIP, String clientID, String clientVote) {
		IP = clientIP;
		ID = clientID;
		vote = clientVote;
	}
	
	// Vote constructor (when NOT provided ID)
	public Vote(String clientIP, String clientVote) {
		IP = clientIP;
		vote = clientVote;
	}
	
	
	
	
}
