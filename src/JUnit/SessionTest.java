package JUnit;

import org.junit.*;

import session.Session;

public class SessionTest {

	private Session s;
	
	@Before
	public void setup() throws Throwable {
		s = new Session();
	}
	
	@org.junit.Test(timeout=1000)
	public void Test1() throws Throwable {
		
		s.addClient("Nick");
		
		if (s.addClient("Nick")) {
			org.junit.Assert.fail("Nick got added to the session twice!");
		}
	}
	
	@org.junit.Test(timeout=1000)
	public void Test2() throws Throwable {
		
		try {
			s.getCurrentImageID();
		} catch (Exception e) {
			if (!(e instanceof NullPointerException)) {
				org.junit.Assert.fail("No null pointer thrown!");
			}
		}
		
	}
	
}
