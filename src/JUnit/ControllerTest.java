package JUnit;

import org.junit.*;

import controller.Controller;
import session.Question;
import session.Session;

public class ControllerTest {

	private Controller c;
	private Session s;
	private byte[] characters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private byte[] fakeMsg;

	@Before
	public void setup() {
		c = new Controller(s = new Session("test"));
		fakeMsg = new byte[1024];
	}

	@org.junit.Test(timeout = 5000)
	public void Test1() throws Throwable {

		byte[] data = c.parseNetworkCommand(fakeMsg);

		if (data[0] != 'E') {
			org.junit.Assert.fail("Doesn't reply error to empty packet");
		}
	}

	@org.junit.Test(timeout = 5000)
	public void Test2() throws Throwable {

		for (int i = 0; i < characters.length; i++) {
			fakeMsg[0] = characters[i];

			byte[] data = c.parseNetworkCommand(fakeMsg);

			if (data[0] != 'E') {
				org.junit.Assert.fail("Doesn't reply error to bad packet");
			}
		}
	}
	
	@org.junit.Test(timeout = 5000)
	public void Test3() throws Throwable {
		
		for (int i = 0; i < 1000; i++) {
			
			fakeMsg = new byte[1024];
			int l = (int)(Math.random() * 50) + 1;
			
			fakeMsg[0] = 'R';
			fakeMsg[1] = (byte) l;
			
			for (int j = 0; j < l; j++) {
				fakeMsg[2 + j] = characters[(int)(Math.random() * 25)];
			}
			
			byte[] data = c.parseNetworkCommand(fakeMsg);
			
			if (data[0] != 'R' && data[1] != (byte) 4 && data[2] != 't' && data[3] != 'e' && data[4] != 's' && data[5] != 't') {
				org.junit.Assert.fail("Doesn't properly add handshake");
			}	
		}
	}
	
	@org.junit.Test(timeout = 5000)
	public void Test4() throws Throwable {
		
		s.currentQuestion = new Question(s, s.loadImage("testimage.jpg"), 123);
		
		fakeMsg[0] = 'M';
		fakeMsg[1] = 'P';
		
		byte[] data = c.parseNetworkCommand(fakeMsg);
		
		if (data[0] != 'M' && data[1] != 'P' && data[2] != (byte) 123) {
			org.junit.Assert.fail("Doesn't properly reply to media ping");
		}
	}
}
