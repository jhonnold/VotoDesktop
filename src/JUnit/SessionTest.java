package JUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.junit.*;

import session.Client;
import session.Session;
import session.Vote;

public class SessionTest {

	private Session s;

	private ArrayList<String> nameList;

	@Before
	public void setup() throws Throwable {
		s = new Session("test");
		nameList = new ArrayList<String>();

		for (int i = 0; i < 100;) {
			String str = getSaltString();
			if (!nameList.contains(str)) {
				nameList.add(str);
				i++;
			}
		}

	}

	@org.junit.Test(timeout = 5000)
	public void Test1() throws Throwable {

		boolean error = false;
		for (String name : nameList) {
			if (!s.addClient(name, name)) {
				error = true;
				break;
			}
		}

		if (error) {
			Assert.fail("One of the valid names could not be added!");
		}

	}

	@org.junit.Test(timeout = 5000)
	public void Test2() throws Throwable {

		boolean error = false;
		for (String name : nameList) {
			s.addClient(name, name);
		}

		for (String name : nameList) {
			if (s.addClient(name, name)) {
				error = true;
				break;
			}
		}

		if (error) {
			Assert.fail("Was able to add multiple clients with same ID!");
		}

	}

	@org.junit.Test(timeout = 5000)
	public void Test3() throws Throwable {

		boolean error = false;
		for (String name : nameList) {
			s.addClient(name, name);
		}

		for (String name : nameList) {
			Client c = s.getClient(name);

			if (c == null || !c.getID().equals(name)) {
				System.out.println(name);
				error = true;
				break;
			}
		}

		if (error) {
			Assert.fail("getClient fails to retrun correct clients!");
		}
	}

	@org.junit.Test(timeout = 5000)
	public void Test4() throws Throwable {

		boolean error = false;
		for (String name : nameList) {
			Client c = s.getClient(name);

			if (c != null) {
				error = true;
				break;
			}
		}

		if (error) {
			Assert.fail("getClient is returning non-existant clients!");
		}
	}

	@org.junit.Test(timeout = 5000)
	public void Test5() throws Throwable {

		boolean error = false;

		for (int i = 0; i < 30; i++) {
			s.addClient(nameList.get(i), nameList.get(i));
		}

		for (int i = 30; i < nameList.size(); i++) {

			Client c = s.getClient(nameList.get(i));

			if (c != null) {
				error = true;
				break;
			}

		}

		if (error) {
			Assert.fail("getClient returning incorrect clients!");
		}

	}

	@org.junit.Test(timeout = 5000)
	public void Test6() throws Throwable {

		boolean error = false;

		String vote = "ABCDE";
		Random rnd = new Random();

		for (String name : nameList) {
			s.addClient(name, name);
		}

		s.setCurrentQuestion("testimage.jpg", "A");

		for (String name : nameList) {

			int index = (int) (rnd.nextFloat() * vote.length());
			if (!s.addClientVote(name, String.valueOf(vote.charAt(index)), 10)) {
				error = true;
				break;
			}
		}

		if (error) {
			Assert.fail("Failed to accept a valid clients vote!");
		}
	}

	@org.junit.Test(timeout = 5000)
	public void Test7() throws Throwable {

		boolean error = false;

		s.setCurrentQuestion("testimage.jpg", "A");

		for (String name : nameList) {

			if (s.addClientVote(name, "A", 10)) {
				error = true;
			}

		}

		if (error) {
			Assert.fail("Server accepts votes from non-handshaked clients!");
		}
	}

	@org.junit.Test(timeout = 5000)
	public void Test8() throws Throwable {

		boolean error = false;
		String vote = "ABCDE";
		Random rnd = new Random();

		s.setCurrentQuestion("testimage.jpg", "A");

		for (String name : nameList) {
			s.addClient(name, name);
		}

		for (String name : nameList) {

			int index = (int) (rnd.nextFloat() * vote.length());
			s.addClientVote(name, String.valueOf(vote.charAt(index)), 10);

			if (!s.getClient(name).getLastVote().getID().equals(String.valueOf(vote.charAt(index)))) {
				error = true;
				break;
			}

		}

		if (error) {
			Assert.fail("Last vote is stored improperly");
		}
	}

	@org.junit.Test(timeout = 5000)
	public void Test9() throws Throwable {

		boolean error = false;
		String vote = "ABCDE";
		Random rnd = new Random();
		int voteNum = 0;

		HashMap<String, ArrayList<String>> votes = new HashMap<>();

		for (String name : nameList) {
			s.addClient(name, name);
			votes.put(name, new ArrayList<String>());
		}

		for (int i = 0; i < 250; i++) {
			s.setCurrentQuestion("testimage.jpg", "A");
			for (String name : nameList) {

				int index = (int) (rnd.nextFloat() * vote.length());
				String v = String.valueOf(vote.charAt(index));

				s.addClientVote(name, v, voteNum++);
				votes.get(name).add(v);
			}
		}

		for (String name : nameList) {
			int i = 0;
			ArrayList<String> expectedVotes = votes.get(name);
			for (Vote v : s.getClient(name).getClientVoteList()) {

				if (!v.getID().equals(expectedVotes.get(i++))) {
					error = true;
				}

			}

			if (i != expectedVotes.size() - 1) {
				error = true;
			}

		}

		if (error) {
			Assert.fail("Vote history not properly returned");
		}
	}

	@org.junit.Test(timeout = 10000)
	public void Test10() throws Throwable {

		boolean error = false;
		String vote = "ABCDE";
		Random rnd = new Random();
		int voteNum = 0;

		for (String name : nameList) {
			s.addClient(name, name);
		}
		
		
		
		for (int i = 0; i < 100; i++) {
			
			s.setCurrentQuestion("testimage.jpg", "A");
			
			int a = 0;
			int b = 0;
			int c = 0;
			int d = 0;
			int e = 0;

			for (String name : nameList) {

				int index = (int) (rnd.nextFloat() * vote.length());
				String v = String.valueOf(vote.charAt(index));
				s.addClientVote(name, v, voteNum++);

				index = (int) (rnd.nextFloat() * vote.length());
				v = String.valueOf(vote.charAt(index));
				s.addClientVote(name, v, voteNum++);

				index = (int) (rnd.nextFloat() * vote.length());
				v = String.valueOf(vote.charAt(index));
				s.addClientVote(name, v, voteNum++);
				
				switch (v) {
					case "A":
						a++;
						break;
					case "B":
						b++;
						break;
					case "C":
						c++;
						break;
					case "D":
						d++;
						break;
					case "E":
						e++;
						break;
				}
			}
			
			HashMap<Vote, ArrayList<Client>> answers = s.getCurrentQuestion().getAnswerSet();
			HashMap<String, Vote> choices = s.getCurrentQuestion().getChoices();
			
			if (a != answers.get(choices.get("A")).size()) {
				error = true;
			}
			if (b != answers.get(choices.get("B")).size()) {
				error = true;
			}
			if (c != answers.get(choices.get("C")).size()) {
				error = true;
			}
			if (d != answers.get(choices.get("D")).size()) {
				error = true;
			}
			if (e != answers.get(choices.get("E")).size()) {
				error = true;
			}
		}
		
		if (error) {
			Assert.fail("Failed");
		}

	}

	// http://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
	// Quick way to generate random strings
	protected String getSaltString() {

		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();

		while (salt.length() < rnd.nextInt(20) + 10) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}

		String saltStr = salt.toString();
		return saltStr;

	}

}
