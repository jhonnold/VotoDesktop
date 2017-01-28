public class main {
	
	public static void main(String[] args) {
		
		udp_server s1 = new udp_server(5555);
		
		udp_client s2 = new udp_client(5555);
		
		s1.start();
		s2.start();
	}
	
}
