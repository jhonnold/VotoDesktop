
public class Server {
	
	public static void main(String[] args) {
		
		Thread server = new Thread(new UDPServer(9876));
		
		server.start();
		
	}
	
}
