package networking.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPSocketListener implements Runnable {
	
	private final static Logger LOG = Logger.getLogger(UDPSocketListener.class.getName());
	
	MulticastSocket socket = null;
	UDPSocket observer;
	
	private InetAddress MULTICASTGROUP;  
	
	public UDPSocketListener(UDPSocket observer, MulticastSocket socket) {
			
		LOG.setLevel(Level.INFO);
		LOG.info("Creating UDPDiscovery on port " + socket.getPort());
		
		this.observer = observer;
		this.socket = socket;
		
		try {
			MULTICASTGROUP = InetAddress.getByName("224.0.1.35");
		} catch (UnknownHostException e) {
			LOG.severe("The multicast group 224.0.1.35 is invalid on this router");
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		try {
			
			socket.joinGroup(MULTICASTGROUP);
			LOG.info("Joined multicast group " + MULTICASTGROUP.getHostAddress());
			
			while (true) {
				LOG.info("Currently listening on port " + socket.getPort());
			
				byte[] buffer = new byte[1024];
				DatagramPacket inFromClient = new DatagramPacket(buffer, buffer.length);
				socket.receive(inFromClient);
				
				LOG.info("Received a packet!");
				
				observer.onPacketReceived(inFromClient);
				
			}
			
		} catch (Exception e) {
			
		}
		
	}
	
}
