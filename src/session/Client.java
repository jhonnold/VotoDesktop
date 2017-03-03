package session;

import java.util.ArrayList;

public class Client {

	private String ID = null;
	
	private ArrayList<Integer> voteList = new ArrayList<>();
	
	public Client(String clientID) {
		ID = clientID;
	}

	public Integer getLastVote() {
		return voteList.get(voteList.size() - 1);
	}
	
	public void setLastVote(Integer lastVote) {
		voteList.add(lastVote);
	}
	
	public boolean equals(String ID) {
		return this.ID.equals(ID);
	}
}
