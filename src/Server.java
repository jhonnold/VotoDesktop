
public class Server {
	
	public static void main(String[] args) {
		
		Thread server = new Thread(new UDPMulticastServer(9876, "233.0.0.1"));
		
		server.start();
		
	}
	
}
