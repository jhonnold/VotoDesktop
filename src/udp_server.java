import java.io.*;
import java.net.*;
 
public class udp_server extends Thread {	
	
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
			
				echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
				
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