package session;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionData {

	private Question currentQuestion;
	private HashMap<Vote, ArrayList<Client>> questionAnswerData = new HashMap<>();
	
	/**
	 * QuestionData constructor
	 */
	public QuestionData() {
		questionAnswerData = new HashMap<>();
	}
	
	/**
	 * Gets the vote/client data for the current question
	 * Will be used to create a list of question data for a session
	 */
	public void getQuestionVoteDate() {
		questionAnswerData = currentQuestion.getAnswerSet();
	}
	
}
