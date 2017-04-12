package session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Question class which holds the active image data, vote list, choices, and
 * answers
 * 
 * @author Nick
 *
 */
public class Question {

	// The correct answer (UNUSED)
	private Vote answer;
	// The imageID to identify questions separate from one another
	private int imageID;
	// The HashMap of answers. It has the list of votes as keys, and when a
	// Client
	// votes for that choice, they are added to the arraylist that holds all
	// other
	// clients who have also voted for that.
	private HashMap<Vote, ArrayList<Client>> answerSet = new HashMap<Vote, ArrayList<Client>>();
	// Maps strings to votes
	private HashMap<String, Vote> choices = new HashMap<>();
	// The ArrayList that holds the question media as a bunch of byte arrays
	public ArrayList<byte[]> questionImg = new ArrayList<byte[]>();

	/**
	 * Question constructor. Takes in the media data as an ArrayList of byte
	 * arrays. It also takes in the imageID. This is just an incremented number.
	 *
	 * @param img
	 *            - Image data as a bunch of byte arrays that are associated
	 *            with this question.
	 * @param imageID
	 *            - The ID for this question (SHOULD BE UNIQUE).
	 */
	public Question(ArrayList<byte[]> img, int imageID) {

		questionImg = img;
		this.imageID = imageID;
		choices.put("A", new Vote("A"));
		choices.put("B", new Vote("B"));
		choices.put("C", new Vote("C"));
		choices.put("D", new Vote("D"));
		choices.put("E", new Vote("E"));

		answer = choices.get("A");

	}

	/**
	 * Sets the correct answer for current question
	 * 
	 * @param ans
	 *            - String for the correct answer
	 */
	public void setAnswer(String ans) {
		answer = choices.get(ans);
	}

	/**
	 * Returns the correct answer for this question
	 * 
	 * @return the Vote corresponding to the correct answer
	 */
	public Vote getAnswer() {
		return answer;
	}

	/**
	 * Returns the mapping of clients to their votes for the current question
	 * 
	 * @return HashMap of votes listing the clients who selected it
	 */
	public HashMap<Vote, ArrayList<Client>> getAnswerSet() {
		return answerSet;
	}

	/**
	 * Gets the possible choices for the current question.
	 * 
	 * @return The mapping of strings to votes that represents the possible
	 *         choices.
	 */
	public HashMap<String, Vote> getChoices() {
		return choices;
	}

	/**
	 * Returns the image ID for current question
	 * 
	 * @return the image ID
	 */
	public int imageID() {
		return imageID;
	}

	/**
	 * Returns the number of packets to which the media for this question
	 * requires. This is determined by how many byte arrays were sent in during
	 * creation.
	 * 
	 * @return Current image's number of packets
	 */
	public int imageSize() {
		return questionImg.size();
	}

	/**
	 * Adding a vote for a specified client for this particular question. It
	 * first checks to make sure this is a more recent vote by confirming this
	 * voteNum is greater than the previous vote. Then it gets the actual vote
	 * object from the choices mapping. Then it makes sure its a valid vote.
	 * Updates old vote num. Then goes through the old answer set, removes the
	 * old vote, and then adds the new vote into the new mapping.
	 * 
	 * @param client
	 *            - The client that voted
	 * @param clientVote
	 *            - The client's vote as a string
	 * @param voteNum
	 *            - The vote number to confirm this is a newer vote.
	 * @return True if the vote is accepted, False if rejected.
	 */
	public boolean addVote(Client client, String clientVote, int voteNum) {

		// Confirm the new vote is in order
		if (voteNum > client.voteNum) {

			Vote v = choices.get(clientVote);

			// if this vote isn't option its invalid
			if (v == null) {
				System.out.println("Vote is invalid!");
				return false;
			}

			client.voteNum = voteNum;

			Vote lastVote = client.getLastVote();

			// If they have a previous vote for this question, update it
			if (lastVote != null) {
				ArrayList<Client> temp = answerSet.get(lastVote);
				if (temp != null) {
					System.out.println("Removed " + client.getID() + " from old vote of " + lastVote.getID());
					temp.remove(client);
				}
			}

			ArrayList<Client> voters = answerSet.get(v);

			// Make the arraylist if no one has answered this one before
			if (voters == null) {
				System.out.println("New Vote Answer Recieved!");
				voters = new ArrayList<Client>();
				answerSet.put(v, voters);
			}

			// Add
			voters.add(client);
			client.setLastVote(v);

			System.out.println("Added vote for " + client.getID() + ": " + clientVote);

			return true;
		} else {
			System.out.println("Votenum is less than previous vote: " + voteNum + " " + client.voteNum);
			return false;
		}
	}

	/**
	 * When a question is finished, this method is to be called. This will go
	 * through all of the clients who voted and then set their final vote for
	 * this particular question in the clients vote List.
	 */
	public void endQuestion() {
		// Set the last vote of every client to their final vote
		for (Vote v : answerSet.keySet()) {
			for (Client c : answerSet.get(v)) {
				c.setAnswerVote(this, v);
			}
		}
	}

	/**
	 * Returns a byte array containing the image for the current question
	 * 
	 * @param packetNum
	 *            - the packet number for the desired image
	 * @return - Image byte array for the packet requested
	 * @throws IllegalArgumentException
	 *             - If this packet request is out of bounds.
	 */
	public byte[] getImagePacket(int packetNum) throws IllegalArgumentException {

		if (packetNum < 1 || packetNum >= questionImg.size() + 1) {
			throw new IllegalArgumentException("Packet number is invalid for this image");
		}

		return questionImg.get(packetNum - 1);
	}
}