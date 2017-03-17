package JUnit;

import org.junit.*;

import session.Client;

public class ClientTest {

	@org.junit.Test(timeout=1000)
	public void Test1() throws Throwable {
		
		Client c = new Client("Nick");
		
		if (!c.equals(new Client("Nick"))) {
			org.junit.Assert.fail("Nick is not registered as the same client!");
		}
	}
	
	@org.junit.Test(timeout=1000)
	public void Test2() throws Throwable {
		
		Client c = new Client("Nick");
		
		if (c.equals(new Client("Jay"))) {
			org.junit.Assert.fail("Jay and Nick are thought as same client!");
		}
		
	}
	
}
