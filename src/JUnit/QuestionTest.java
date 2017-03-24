package JUnit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import session.Question;
import session.Session;

public class QuestionTest {

	Session s;
	Question q;
	
	@Before
	public void setup() throws Throwable {
		s = new Session("test");
		q = new Question(s.loadImage("testImage.png"), 123);
	}
	
	@Test
	public void test1() {
		assertEquals(q.imageID(), 123);
	}
}
