package testing;

import java.net.*;

public class UDPClient implements Runnable {
	
	DatagramSocket socket;
	private InetAddress GROUP = null;
	
	private final int PORT;
	
	
	public UDPClient(int p) {
		PORT = p;
		try {
			GROUP = InetAddress.getByName("localhost");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		try {
			socket = new DatagramSocket();
			
			byte[] send = "teestedfs".getBytes();
			
			try {
				DatagramPacket dp = new DatagramPacket(send, send.length, GROUP, PORT);
				socket.send(dp);
				
				System.out.println(getClass().getName() + ">>> Request packet sent to " + GROUP.getHostAddress());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			System.out.println(getClass().getName() + ">>> Waiting for a reply...");
			
			byte[] buffer = new byte[1024];
			DatagramPacket rp = new DatagramPacket(buffer, buffer.length);
			socket.receive(rp);
			
			System.out.println(getClass().getName() + ">>> Got a reply from: " + rp.getAddress().getHostAddress());
			System.out.println(getClass().getName() + ">>> Received: " + new String(rp.getData()).trim());
			
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
