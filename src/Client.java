
public class Client {
	
	public static void main(String[] args) {
		
		Thread client = new Thread(new UDPMulticastClient(9876, "233.0.0.1"));
		
		client.start();
		
	}
	
}
