package session;

import java.util.ArrayList;

public class Client {

	private String ID = null;
	private ArrayList<Vote> voteList = new ArrayList<>();
	
	/**
	 * Client constructor
	 * @param clientID - this client's ID
	 */
	public Client(String clientID) {
		ID = clientID;
	}

	/**
	 * Returns the last vote sent by the specified client
	 * @return last received vote
	 */
	public Vote getLastVote() {
		if (voteList.size() > 0) {
			return voteList.get(voteList.size() - 1);
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the clients most recently sent vote as their current vote
	 * @param lastVote - the most recently sent vote
	 * @param oldVote - reference to previous client vote to be discarded
	 */
	public void setLastVote(Vote lastVote, Vote oldVote) {
		if (voteList.contains(oldVote)) {
			voteList.remove(oldVote);
		}
		voteList.add(lastVote);
	}
	
	/**
	 * Tells whether or not the sent arg is the current client
	 * @param ID - a client ID to validate
	 * @return True if the ID sent in matches the current client's ID
	 *         False if the IDs don't match
	 */
	public boolean equals(String ID) {
		return this.ID.equals(ID);
	}
}
