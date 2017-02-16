package session;

import java.util.HashMap;

import testing.Server;

public class Session {
	
	private HashMap<String, HashMap<String, String>> questions = new HashMap<>();
	private String currentQuestion;
	
	public void askQuestion(String q) {
		currentQuestion = q;
		questions.put(q, new HashMap<String, String>());
	}
	
	public void addAnswer(String ID, String ans) {
		HashMap<String, String> temp = questions.get(currentQuestion);
		System.out.println("Got an answer for the question: " + currentQuestion);
		
		if (temp.keySet().contains(ID)) {
			temp.replace(ID, ans);
			System.out.println(ID + " has changed their answer to: " + ans);
		} else {
			temp.put(ID, ans);
			System.out.println(ID + " has submitted their answer of: "+ ans);
		}
	}
}
