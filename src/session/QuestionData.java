package session;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionData {

	private Question currentQuestion;
	private int ID;
	private HashMap<Vote, ArrayList<Client>> questionAnswerData = new HashMap<>();
	
	/**
	 * QuestionData constructor
	 */
	public QuestionData(Question q, int qID) {
		currentQuestion = q;
		ID = qID;
	}
	
	/**
	 * Gets the vote/client data for the current question
	 * Will be used to create a list of question data for a session
	 */
	public HashMap<Vote, ArrayList<Client>> getQuestionVoteDate() {
		questionAnswerData = currentQuestion.getAnswerSet();
		return questionAnswerData;
	}
	
	
	
}
