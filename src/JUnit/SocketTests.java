package JUnit;

import java.net.SocketException;

import org.junit.*;

import networking.UDPSocket;

public class SocketTests {
	
	private UDPSocket s;
	
	@Before
	public void setup() throws Throwable {
		s = new UDPSocket(null);
		new Thread(s).start();
	}
	
	@After
	public void post() throws Throwable {
		s.close();
	}
	
	@org.junit.Test(timeout=5000)
	public void Test1() throws Throwable {
		
		boolean thrown = false;
		
		try {
			new Thread(new UDPSocket(null)).start();
		} catch (Exception e) {
			thrown = true;
			org.junit.Assert.assertThat("Second sockets throws Socket Exception", e, org.hamcrest.CoreMatchers.instanceOf(SocketException.class));
		}
		
		if (!thrown) {
			org.junit.Assert.fail("Second socket doesn't throw Socket Exception");
		}
		
	}
	
	@org.junit.Test(timeout=5000)
	public void Test2() throws Throwable {
		
		s.close();
		
		if (s.isListening()) {
			org.junit.Assert.fail("Socket doesn't close!");
		}
		
	}
}
