package JUnit;

import org.junit.*;

import session.Vote;

public class VoteTest {

	@org.junit.Test(timeout=1000)
	public void Test1() throws Throwable {
		
		Vote v = new Vote("A");
		
		if (v.equals(new Vote("B"))) {
			org.junit.Assert.fail("Vote A and B registered equal!");
		}
	}
	
	@org.junit.Test(timeout=1000)
	public void Test2() throws Throwable {
		
		Vote v = new Vote("A");
		
		if (!v.equals(new Vote("A"))) {
			org.junit.Assert.fail("Vote 1 and 1 registered unequal!");
		}
		
	}
}
