import java.net.*;

public class TCPClient {
	
	public static void main(String[] args) {
		
		try {
			
			Socket s = new Socket("192.168.1.51", 9876);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}