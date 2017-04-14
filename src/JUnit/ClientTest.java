package JUnit;

import org.junit.*;

import session.Client;

public class ClientTest {

	@org.junit.Test(timeout=1000)
	public void Test1() throws Throwable {
		
		Client c = new Client("Nick", "111.111.111.111");
		
		if (!c.equals(new Client("Nick", "111.111.111.111"))) {
			org.junit.Assert.fail("Nick is not registered as the same client!");
		}
	}
	
	@org.junit.Test(timeout=1000)
	public void Test2() throws Throwable {
		
		Client c = new Client("Nick", "111.111.111.111");
		
		if (c.equals(new Client("Jay", "123.123.123.123"))) {
			org.junit.Assert.fail("Jay and Nick are thought as same client!");
		}
		
	}
	
}
