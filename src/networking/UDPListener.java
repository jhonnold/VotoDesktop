package networking;

import java.net.DatagramPacket;
import java.lang.reflect.Method;

public class UDPListener implements Runnable {
	
	private UDPSocket socket;
	private Method method;
	
	public UDPListener() {
		this.socket = new UDPSocket(this);
	}
	
	public void onPacketReceived(DatagramPacket inFromClient) {
		
		System.out.println("Got something");
		
		String data = new String(inFromClient.getData()).trim();
		
		String[] kwargs = data.split("_");
		
		try {
			method = this.getClass().getMethod(kwargs[0], DatagramPacket.class, String[].class);
			method.invoke(this, inFromClient, kwargs);
		} catch (Exception e) {
		}
	}
	
	public void handshakeRequest(DatagramPacket dp, String[] kwargs) {
		System.out.println("Got a handshake request");
		socket.send(dp);
	}
	
	public void vote(DatagramPacket dp, String[] kwargs) {
		System.out.println("Got a vote!");
		System.out.println(kwargs[1]);
	}
	
	@Override
	public void run() {
		
		Thread listening = new Thread(socket, "UDPSocket");
		listening.start();
	}
}
