import java.net.*;

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
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
