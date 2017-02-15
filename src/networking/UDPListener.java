package networking;

import java.net.DatagramPacket;
import java.lang.reflect.Method;

public class UDPListener implements Runnable {
	
	private UDPSocket socket;
	private Method method;
	private UDPHandler handler;
	
	public UDPListener() {
		this.socket = new UDPSocket(this);
		this.handler = new UDPHandler(socket);
	}
	
	public void onPacketReceived(DatagramPacket inFromClient) {
		
		System.out.println("Got something");
		
		String data = new String(inFromClient.getData()).trim();
		
		String[] kwargs = data.split("_");
		
		try {
			method = handler.getClass().getMethod(kwargs[0], DatagramPacket.class, String[].class);
			method.invoke(handler, inFromClient, kwargs);
		} catch (Exception e) {
		}
	}
	
	@Override
	public void run() {
		
		Thread listening = new Thread(socket, "UDPSocket");
		listening.start();
	}
}
