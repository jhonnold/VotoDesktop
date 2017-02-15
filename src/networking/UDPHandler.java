package networking;

import java.net.DatagramPacket;

public class UDPHandler {
	
	private UDPSocket socket;
	
	public UDPHandler(UDPSocket socket) {
		this.socket = socket;
	}
	
	public void handshakeRequest(DatagramPacket dp, String[] kwargs) {
		socket.send(dp);
	}
	
}
