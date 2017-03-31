package session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Question class stripped into just the important data
 * to remove usage of RAM
 * @author Nick
 *
 */
public class QuestionData {

	private int ID;
	public HashMap<Vote, ArrayList<Client>> questionAnswerData = new HashMap<>();
	
	/**
	 * QuestionData constructor
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
