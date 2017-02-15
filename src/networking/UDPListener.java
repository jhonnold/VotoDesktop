package networking;

import java.net.DatagramPacket;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UDPListener implements Runnable {
	
	private UDPSocket socket;
	private Method method;
	
	private final String ID;
	
	public UDPListener(String ID) {
		this.socket = new UDPSocket(this);
		this.ID = ID;
	}
	
	public void onPacketReceived(DatagramPacket inFromClient) {
		String data = new String(inFromClient.getData()).trim();
		
		String[] kwargs = data.split("_");
		
		try {
			method = this.getClass().getMethod(kwargs[0], DatagramPacket.class, String[].class);
			method.invoke(this, inFromClient, kwargs);
		} catch (InvocationTargetException e) {
			returnError(inFromClient, e.getCause().getMessage());
		} catch (Exception e) {
		}
	}
	
	public void handshakeRequest(DatagramPacket in, String[] kwargs) {
		System.out.println("Got a handshake request");
		
		byte[] buffer = ("handshakeAccepted_" + ID).getBytes();	
		DatagramPacket out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
		
		socket.send(out);
	}
	
	public void vote(DatagramPacket in, String[] kwargs) throws IllegalArgumentException {
		
		if (kwargs.length == 1) {
			throw new IllegalArgumentException("No vote attached to vote command!");
		}
		
		System.out.print("Received a vote: ");
		System.out.println(kwargs[1]);
		
		byte[] buffer = ("voteAccepted_" + ID).getBytes();
		DatagramPacket out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
		
		socket.send(out);
	}
	
	public void returnError(DatagramPacket in, String message) {
		byte[] buffer = ("ERROR_" + message).getBytes();
		DatagramPacket out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
		
		socket.send(out);
	}
	
	@Override
	public void run() {	
		Thread listening = new Thread(socket, "UDPSocket");
		listening.start();
	}
}
