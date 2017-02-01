import java.net.*;

public class UDPMulticastClient implements Runnable {
	
	MulticastSocket socket;
	
	private final int PORT;
	private final String group;
	
	public UDPMulticastClient(int p, String g) {
		PORT = p;
		group = g;
	}
	
	@Override
	public void run() {
		try {
			socket = new MulticastSocket();
			socket.joinGroup(InetAddress.getByName(group));
			
			byte[] send = "DISCOVER_VOTO_REQUEST".getBytes();
			
			try {
				DatagramPacket dp = new DatagramPacket(send, send.length, InetAddress.getByName("255.255.255.255"), PORT);
				socket.send(dp);
				
				System.out.println(getClass().getName() + ">>> Request packet sent to 255.255.255.255");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			System.out.println(getClass().getName() + ">>> Waiting for a reply...");
			
			byte[] buffer = new byte[1024];
			DatagramPacket rp = new DatagramPacket(buffer, buffer.length);
			socket.receive(rp);
			
			System.out.println(getClass().getName() + ">>> Got a reply from: " + rp.getAddress().getHostAddress());
			System.out.println(getClass().getName() + ">>> Received: " + new String(rp.getData()).trim());
			
			socket.leaveGroup(InetAddress.getByName(group));
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
