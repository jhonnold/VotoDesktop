package session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Question class stripped into just the important data to remove usage of
 * RAM. This class is used mostly for the bringing up previous questions data in
 * the histogram, as well as exporting the session data to a CSV at the end.
 * 
 * @author Nick
 *
 */
public class QuestionData {

	// The original question's ID
	private int ID;
	// The mapping of votes to clients in order to keep track of how
	// many voted for each question.
	public HashMap<Vote, ArrayList<Client>> questionAnswerData = new HashMap<>();

	/**
	 * The QuestionData constructor that takes in a Question object and ID. The
	 * question object is used to simply receive the answer data set. The ID is
	 * then set to the classes ID.
	 * 
	 * @param q
	 *            - The previous question to which to store the answer data of.
	 * @param qID
	 *            - The ID to be associated with this new object.
	 */
	public QuestionData(Question q, int qID) {

		if (q != null) {
			questionAnswerData = q.getAnswerSet();
		}

		ID = qID;
	}

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof QuestionData)) {
			return false;
		}

		if (o == this) {
			return true;
		}

		QuestionData temp = (QuestionData) o;

		return temp.getID() == ID;

	}

	/**
	 * Checks for a given ID and if there is available question data for it
	 * 
	 * @return - the ID we are trying to access question data for
	 */
	public int getID() {
		return ID;
	}

}
