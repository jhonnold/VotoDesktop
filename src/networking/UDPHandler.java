package networking;

import java.net.DatagramPacket;

public class UDPHandler {
	
	private UDPSocket socket;
	
	public UDPHandler(UDPSocket socket) {
		this.socket = socket;
	}
	
	public void handshakeRequest(DatagramPacket dp, String[] kwargs) {
		System.out.println("Got a handshake request");
		socket.send(dp);
	}
	
	public void vote(DatagramPacket dp, String[] kwargs) {
		System.out.println("Got a vote!");
		System.out.println(kwargs[1]);
	}
	
}
