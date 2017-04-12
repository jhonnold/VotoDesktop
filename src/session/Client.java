package session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the Client class, which will be generated for each connected client
 * that handshakes with the server. It stores both the IP as well as a unique
 * ID. If the ID is not specified, then the client is to send the IP as the ID.
 */
public class Client {

	// The ID and the IP
	private String ID = null;
	private String IP = null;

	// This is the voteList of the all the previous votes
	private ArrayList<Vote> voteList = new ArrayList<Vote>();

	// Holds the most recent vote to know what to overwrite
	// if the user votes again
	private Vote lastVote;

	// An increment to know when to overwrite old votes vs new votes
	public int voteNum = -100;

	/**
	 * Client constructor that takes in an ID and IP. The ID is the ID of the
	 * client and should be unique. If its the same, the client should be
	 * rejected. The IP is also tracking who is connected in order to make sure
	 * clients don't change their ID during sessions.
	 * 
	 * @param clientID
	 *            - The Client's unique ID. Should be a string resembling their
	 *            name, or their IP is they didn't specify.
	 * @param IP
	 *            - The Client's IP to store to check to make sure that a user
	 *            isn't changing their name to fake another client
	 */
	public Client(String clientID, String IP) {
		ID = clientID;
		this.IP = IP;
	}

	/**
	 * Gets the client's ID
	 * 
	 * @return the client's ID as a string
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Gets the client's IP
	 * 
	 * @return the client's IP as a string
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * Returns the last vote sent by the specified client
	 * 
	 * @return The last received vote from the client
	 */
	public Vote getLastVote() {
		return lastVote;
	}

	/**
	 * Sets the clients most recently sent vote as their current vote. This is
	 * used so that when a user votes multiple times during a session, this can
	 * be used to remove them from their last vote without having to search
	 * through the entire mapping.
	 * 
	 * @param newVote
	 *            - The new vote of the client that has been stored in the
	 *            mapping
	 */
	public void setLastVote(Vote newVote) {

		lastVote = newVote;
	}

	/**
	 * UNUSED - When a question is finished this method is to mark the client's
	 * final vote for that question and create a mapping from that Question to
	 * that Vote.
	 * 
	 * @param q
	 *            - The Question to which the client has just finished voting
	 *            for.
	 * @param v
	 *            - The Client's final vote for said question.
	 */
	public void setAnswerVote(Question q, Vote v) {
		// voteList.put(q, v);
		voteList.add(v);
	}

	/**
	 * Overwrite of the equals method that is to do the same thing, however, it
	 * matches clients based on IP. Thusly, if multiple clients have the same IP
	 * (which should never happen) and different ID's, they will still return
	 * equal.
	 * 
	 * @return True if the IP sent in matches the current client's IP. False if
	 *         the IPs don't match
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
		return c.IP.equals(IP);
	}

	/**
	 * Returns a list of all the clients final votes for current session as an
	 * Iterable.
	 * 
	 * @return List of client votes
	 */
	public Iterable<Vote> getClientVoteList() {
		return voteList;// .values();
	}
}
