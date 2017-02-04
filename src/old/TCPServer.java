import java.net.*;
import java.io.*;

public class TCPServer implements Runnable {
	
	private final int PORT;
	
	ServerSocket welcomeSocket = null;
	
	public TCPServer(int p) {
		PORT = p;
	}
	
	@Override
	public void run() {
		
		System.out.println(getClass().getName() + " >>> Started");
		
		try {
			welcomeSocket = new ServerSocket(PORT);
			
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println(getClass().getName() + " >>> I have connected to a client!");
			System.out.println(getClass().getName() + " >>> " + connectionSocket.getInetAddress().getHostAddress() + " : " + connectionSocket.getPort());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			String input = br.readLine();
			
			System.out.println(getClass().getName() + " >>> Client says: " + input);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
