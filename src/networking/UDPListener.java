package networking;

import java.net.DatagramPacket;

public class UDPListener {
	
	UDPSocket socket;
	
	public UDPListener() {
		
	}
	
	public void startUDPSocket() {
		socket = new UDPSocket(this);
		
		Thread listening = new Thread(socket, "UDPSocket");
		listening.start();
	}
	
	public void onPacketReceived(DatagramPacket inFromClient) {
		
		socket.close();
		
	}
	
	public static void main(String[] args) {
		
		UDPListener udp = new UDPListener();
		udp.startUDPSocket();
	
	}
}
