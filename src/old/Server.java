
public class Server implements NetworkListener {
	
	private boolean TCP = false;
	
	public static void main(String[] args) {
		
		//Thread server = new Thread(new UDPDiscoveryServer(9876, new Server()));		
		//server.start();
		new Server().startTCP();
	}
	
	public boolean tcpOpen() {
		return TCP;
	}
	
	public void startTCP() {
		
		Thread tcpThread = new Thread(new TCPServer(9876));
		TCP = true;
		tcpThread.start();
	}
}
