import java.io.*;
import java.net.*;

public class udp_client extends Thread {
	
	private int PORT;
	
	private DatagramSocket sock = null;
	
	public udp_client(int p) {
		PORT = p;
	}
	
	@Override
	public void run() {
		
		try {
            sock = new DatagramSocket();
			
            String host = "localhost";
            InetAddress address = InetAddress.getByName(host);
            
            byte[] message = "UDP is da best".getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length, address, PORT);
            
            sock.send(packet);
            
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            
            while (true) {
				sock.receive(incoming);
				byte[] data = incoming.getData();
				String s = new String(data, 0, incoming.getLength());
			
				echo("Client: " + incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
				
				sleep(500);
				
				DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , PORT);
                sock.send(dp);
            }
        } catch (Exception e) {
        	echo(e.toString());
        }
		
	}
	
	public static void echo(String msg) {
        System.out.println(msg);
    }
}
