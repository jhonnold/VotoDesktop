
public class Client {
	
	public static void main(String[] args) {
		
		Thread client = new Thread(new UDPClient(9876));
		
		client.start();
		
	}
	
}
