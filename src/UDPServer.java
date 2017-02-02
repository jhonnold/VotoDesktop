import java.net.*;

public class UDPServer implements Runnable {
	
	MulticastSocket socket = null;
	
	private final int PORT;
	private InetAddress GROUP = null;
	
	public UDPServer(int p) {
		PORT = p;
		try {
			GROUP = InetAddress.getByName("224.0.0.3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		try {
			
			socket = new MulticastSocket(PORT);
			socket.joinGroup(GROUP);
			
			while (true) {
				System.out.println(getClass().getName() + ">>> Ready to receive broadcast packets!");
				
				byte[] buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				socket.receive(dp);
				
				System.out.println(getClass().getName() + ">>> Discovery packet received from: " + dp.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>> Packet received; data: " + new String(dp.getData()));
				
				String msg = new String(dp.getData()).trim();
				
				if (msg.equals("VOTO_HANDSHAKE_REQUEST")) {
					byte[] send = "VOTO_HANDSHAKE_RESPONSE".getBytes();
					
					DatagramSocket ds = new DatagramSocket();
					
					DatagramPacket sp = new DatagramPacket(send, send.length, dp.getAddress(), dp.getPort());
					socket.send(sp);
					
					System.out.println(getClass().getName() + ">>> Sent packet to: " + sp.getAddress().getHostAddress());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
