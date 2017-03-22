package JUnit;

import java.util.ArrayList;
import java.util.Random;

import org.junit.*;

import session.Client;
import session.Question;
import session.Session;

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
			if (!s.addClient(name)) {
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
			s.addClient(name);
		}

		for (String name : nameList) {
			if (s.addClient(name)) {
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
			s.addClient(name);
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
			s.addClient(nameList.get(i));
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
			s.addClient(name);
		}
		
		s.newCurrentQuestion("testimage.jpg", "A");
		
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
