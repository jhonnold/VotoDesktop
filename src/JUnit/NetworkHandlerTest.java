package JUnit;

import java.net.SocketException;

import org.junit.*;

import controller.Controller;
import networking.NetworkHandler;
import session.Session;

public class NetworkHandlerTest {
	
	private NetworkHandler nh;
	
	@Before
	public void setup() throws Throwable {
		nh = new NetworkHandler(new Controller(new Session()));
		new Thread(nh).start();
	}
	
	@After
	public void post() throws Throwable {
		nh.close();
	}
	
	@org.junit.Test(timeout=5000)
	public void Test1() throws Throwable {
		
		boolean thrown = false;
		
		try {
			new Thread(new NetworkHandler(null)).start();
		} catch (Exception e) {
			thrown = true;
			org.junit.Assert.assertThat("Second handler throws Socket Exception", e, org.hamcrest.CoreMatchers.instanceOf(SocketException.class));
		}
		
		if (!thrown) {
			org.junit.Assert.fail("Network Handler fails to throw socket exception");
		}
		
	}
	
	@org.junit.Test(timeout=5000)
	public void Test2() throws Throwable {		
		nh.close();
	}
	
}
