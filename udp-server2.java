import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.io.*;
import java.net.*;

public class udp-server2 {
    
    public static void main(String args[]) {
        try {
            String host = "141.219.236.182";
            int port = 5555;
            
            byte[] message = "UDP is da best".getBytes();
            
            // Get the internet address of the specified host
            InetAddress address = InetAddress.getByName(host);
            
            // Initialize a datagram packet with data and address
            DatagramPacket packet = new DatagramPacket(message, message.length,
                                                       address, port);
            
            // Create a datagram socket, send the packet through it, close it.
            DatagramSocket dsocket = new DatagramSocket();
            
            //buffer to receive incoming data
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            
            while(true){
                
                dsocket.send(packet);
                dsocket.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                
                //echo the details of incoming data - client ip : client port - client message
                System.out.println(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
                
                s = "OK : " + s;
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
