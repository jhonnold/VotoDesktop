import java.net.*;
import java.io.*;

public class TCPClient {
	
	public static void main(String[] args) {
		
		try {
			
			Socket s = new Socket("localhost", 9876);
			
			DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());
			
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			String msg = inFromUser.readLine();
			
			outToServer.writeBytes(msg);
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}