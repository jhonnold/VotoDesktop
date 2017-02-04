package reference;

import java.io.*;
import java.net.*;
 
public class udp_server implements Runnable {	
	
	private int PORT;
	
	private DatagramSocket sock = null;
	
	public udp_server(int p) {
		PORT = p;
	}
	
	public void run() {
		
		try {
			sock = new DatagramSocket(PORT);
			
			byte[] buffer = new byte[65536];
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			
			echo("Server socket created. Waiting for incoming data...");
		
			while (true) {
				sock.receive(incoming);
				byte[] data = incoming.getData();
				String s = new String(data, 0, incoming.getLength());
			
				echo("Server: " + incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
				
				DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
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