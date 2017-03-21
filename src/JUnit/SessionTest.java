package JUnit;

import org.junit.*;

import session.Question;
import session.Session;

public class SessionTest {

	private Session s;
	
	@Before
	public void setup() throws Throwable {
		s = new Session("test");
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
	
	@org.junit.Test(timeout=5000)
	public void Test3() throws Throwable {
		
		s.currentQuestion = new Question(s, s.loadImage("testimage.jpg"), 123);
		
		int id = s.getCurrentImageID();
		
		if (id != 123) {
			org.junit.Assert.fail("Image ID is incorrect");
		}
	}
	
	@org.junit.Test(timeout=5000)
	public void Test4() throws Throwable {
		s.currentQuestion = new Question(s, s.loadImage("testimage.jpg"), 123);
		
		int packets = s.getCurrentImagePacketCount();
		
		if (packets != 1) {
			org.junit.Assert.fail("Image packet count incorrect");
		}
	}
	
	@org.junit.Test(timeout=5000)
	public void Test5() throws Throwable {
		s.currentQuestion = new Question(s, s.loadImage("testimage.jpg"), 123);
		
		int size = s.getCurrentImageSize();
		
		if (size != 14549) {
			org.junit.Assert.fail("Image size is incorrect");
		}
	}
	
}
