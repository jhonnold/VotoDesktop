import java.net.*;

public class UDPMulticastServer implements Runnable {
	
	MulticastSocket socket;
	
	private final int PORT;
	private final String group;
	
	public UDPMulticastServer(int p, String g) {
		PORT = p;
		group = g;
	}
	
	@Override
	public void run() {
		
		try {
			socket = new MulticastSocket(PORT);
			socket.joinGroup(InetAddress.getByName(group));
			
			while (true) {
				System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");
				
				byte[] buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				socket.receive(dp);
				
				System.out.println(getClass().getName() + ">>>Discovery packet received from: " + dp.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(dp.getData()));
				
				String msg = new String(dp.getData()).trim();
				
				if (msg.equals("DISCOVER_VOTO_REQUEST")) {
					byte[] send = "DISCOVER_VOTO_RESPONSE".getBytes();
					
					DatagramPacket sp = new DatagramPacket(send, send.length, dp.getAddress(), dp.getPort());
					socket.send(sp);
					
					System.out.println(getClass().getName() + ">>>Sent packet to: " + sp.getAddress().getHostAddress());
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
