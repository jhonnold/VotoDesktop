package JUnit;

import org.junit.*;

import session.Vote;

public class VoteTest {

	@org.junit.Test(timeout=1000)
	public void Test1() throws Throwable {
		
		Vote v = new Vote(1);
		
		if (v.equals(new Vote(2))) {
			org.junit.Assert.fail("Vote 1 and 2 registered equal!");
		}
	}
	
	@org.junit.Test(timeout=1000)
	public void Test2() throws Throwable {
		
		Vote v = new Vote(1);
		
		if (!v.equals(new Vote(1))) {
			org.junit.Assert.fail("Vote 1 and 1 registered unequal!");
		}
		
	}
}
