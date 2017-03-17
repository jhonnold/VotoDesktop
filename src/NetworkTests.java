import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.*;

public class NetworkTests {
	
	private InetAddress GROUP = null;
	private int PORT = 9876;
	private DatagramSocket socket = null;
	private DatagramPacket out = null;
	private DatagramPacket in = null;
	
	private byte[] characters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
								 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	
	
	@Before
	public void setup() throws Throwable {
		GROUP = InetAddress.getByName("localhost");
		socket = new DatagramSocket();
	}
	
	@org.junit.Test(timeout = 12000)
	public void Test1() throws Throwable {
		
		{
			for (int i = 0; i < characters.length; i++) {
				byte[] msg = new byte[1];
				msg[0] = characters[i];
				out = new DatagramPacket(msg, msg.length, GROUP, PORT);
				socket.send(out);
			
				byte[] buffer = new byte[1024];
				in = new DatagramPacket(buffer, buffer.length);
				socket.receive(in);
			
				byte[] reply = in.getData();
			
				if ((char) reply[0] != 'E') {
					org.junit.Assert.fail("Sending " + (char) characters[i] + " doesn't return an error");
				}
				
				Thread.sleep(10);
			}
		}
		
	}
	
	@org.junit.Test(timeout = 60000)
	public void Test2() throws Throwable {
		
		{
			for (int i = 1; i <= 100; i++) {	
				byte[] msg = {(byte) 'R', (byte) i};
				out = new DatagramPacket(msg, msg.length, GROUP, PORT);
				socket.send(out);
			
				byte[] buffer = new byte[1024];
				in = new DatagramPacket(buffer, buffer.length);
				socket.receive(in);
			
				byte[] reply = in.getData();
			
				if ((char) reply[0] != 'E') {
					org.junit.Assert.fail("Sending bad handshake 'R3' doesn't return an error");
				}
				Thread.sleep(10);
			}
		}
	}
	
	@org.junit.Test(timeout = 60000)
	public void Test3() throws Throwable {
		
		{
			for (int i = 1; i < 100; i++) {	
				int l = (int)(Math.random() * 20) + 1;
				byte[] msg = new byte[l + 2];
				msg[0] = 'R';
				msg[1] = (byte) l;
				
				for (int j = 0; j < l; j++) {
					msg[2 + j] = characters[(int)(Math.random() * 26)];
				}
				
				out = new DatagramPacket(msg, msg.length, GROUP, PORT);
				socket.send(out);
			
				byte[] buffer = new byte[1024];
				in = new DatagramPacket(buffer, buffer.length);
				socket.receive(in);
			
				byte[] reply = in.getData();
			
				if (reply[0] != (byte) 'R') {
					org.junit.Assert.fail("Sending handshake doesn't return properly " + (char) reply[0]);
				}
				Thread.sleep(10);
			}
		}
	}
	
	@org.junit.Test(timeout = 60000)
	public void Test4() throws Throwable {
		
		{
			for (int i = 1; i < 100; i++) {	
				byte[] msg = new byte[2];
				msg[0] = 'M';
				msg[1] = 'P';
				
				out = new DatagramPacket(msg, msg.length, GROUP, PORT);
				socket.send(out);
			
				byte[] buffer = new byte[1024];
				in = new DatagramPacket(buffer, buffer.length);
				socket.receive(in);
			
				byte[] reply = in.getData();
			
				if (reply[0] != (byte) 'E') {
					org.junit.Assert.fail("Sending handshake doesn't return properly " + (char) reply[0]);
				}
				Thread.sleep(10);
			}
		}
	}
	
}
