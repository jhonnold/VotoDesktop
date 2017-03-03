package session;

import java.util.ArrayList;

public class Client {

	private String ID = null;
	
	private ArrayList<Vote> voteList = new ArrayList<>();
	
	public Client(String clientID) {
		ID = clientID;
	}

	public Vote getLastVote() {
		if (voteList.size() > 0) {
			return voteList.get(voteList.size() - 1);
		} else {
			return null;
		}
	}
	
	public void setLastVote(Vote lastVote, Vote oldVote) {
		if (voteList.contains(oldVote)) {
			voteList.remove(oldVote);
		}
		voteList.add(lastVote);
	}
	
	public boolean equals(String ID) {
		return this.ID.equals(ID);
	}
}
