package session;

import java.util.ArrayList;

public class Client {

	private String ID = null;
	private ArrayList<Vote> voteList = new ArrayList<>();
	
	
	private Vote lastVote;
	public int voteNum = 0;
	
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
		return lastVote;
	}
	
	/**
	 * Sets the clients most recently sent vote as their current vote
	 * @param lastVote - the most recently sent vote
	 * @param oldVote - reference to previous client vote to be discarded
	 */
	public void setLastVote(Vote newVote) {
		lastVote = newVote;
	}
	
	/**
	 * 
	 * @param v
	 */
	public void setAnswerVote(Vote v) {
		voteList.add(v);
	}
	
	/**
	 * Tells whether or not the sent arg is the current client
	 * @param ID - a client ID to validate
	 * @return True if the ID sent in matches the current client's ID
	 *         False if the IDs don't match
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Client)) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
		Client c = (Client) o;
		return c.ID.equals(ID);
	}
	
	/**
	 * Returns a list of all the clients final votes for current session
	 * @return List of client votes
	 */
	public ArrayList<Vote> getClientVoteList() {
		return this.voteList;
	}
}
