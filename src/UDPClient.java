import java.net.*;


public class UDPClient implements Runnable {
	
	DatagramSocket c;
	
	private final int PORT;
	
	public UDPClient(int p) {
		PORT = p;
	}
	
	public void run() {
		try {
			c = new DatagramSocket();
			c.setBroadcast(true);
			
			byte[] send = "DISCOVER_VOTO_REQUEST".getBytes();
			
			try {
				DatagramPacket dp = new DatagramPacket(send, send.length, InetAddress.getByName("255.255.255.255"), PORT);
				c.send(dp);
				
				System.out.println(getClass().getName() + ">>> Request packet sent to 255.255.255.255");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			System.out.println(getClass().getName() + ">>> Waiting for a reply...");
			
			byte[] buffer = new byte[1024];
			DatagramPacket rp = new DatagramPacket(buffer, buffer.length);
			c.receive(rp);
			
			System.out.println(getClass().getName() + ">>> Got a reply from: " + rp.getAddress().getHostAddress());
			System.out.println(getClass().getName() + ">>> Received: " + new String(rp.getData()).trim());
			
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
