package reference;

public class main {
	
	public static void main(String[] args) {
		
		Thread s1 = new Thread(new udp_server(5555));
		
		Thread s2 = new Thread(new udp_client(5555, 5556));
		Thread s3 = new Thread(new udp_client(5555, 5557));
		
		s1.start();
		s2.start();
		s3.start();
	}
	
}
