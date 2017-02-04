import java.net.*;

public class UDPDiscoveryServer implements Runnable {
	
	MulticastSocket socket = null;
	NetworkListener listener;
	
	
	private final int PORT;
	private InetAddress GROUP = null;
	
	public UDPDiscoveryServer(int p, NetworkListener l) {
		PORT = p;
		listener = l;
		
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
				System.out.println(getClass().getName() + " >>> I will receive packets sent to: 224.0.0.3");
				System.out.println(getClass().getName() + " >>> I will reply to VOTO_HANDSHAKE_REQUEST");
				System.out.println(getClass().getName() + " >>> with VOTO_HANDSHAKE_RESPONSE");
				
				byte[] buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				socket.receive(dp);
				
				System.out.println(getClass().getName() + " >>> Discovery packet received from: " + dp.getAddress().getHostAddress());
				System.out.println(getClass().getName() + " >>> Packet received; Data: " + new String(dp.getData()));
				
				String msg = new String(dp.getData()).trim();
				
				if (msg.equals("VOTO_HANDSHAKE_REQUEST")) {
					byte[] send = "VOTO_HANDSHAKE_RESPONSE".getBytes();
					
					DatagramPacket sp = new DatagramPacket(send, send.length, dp.getAddress(), dp.getPort());
					socket.send(sp);
					
					System.out.println(getClass().getName() + " >>> Sent response packet to: " + sp.getAddress().getHostAddress());
					
					if (!listener.tcpOpen()) {
						
						System.out.println(getClass().getName() + " >>> Starting TCP Server");
						listener.startTCP();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
