package testing;

import java.io.IOException;
import java.net.*;

public class UDPClient implements Runnable {
	
	Media media;
	DatagramSocket socket;
	private InetAddress GROUP = null;
	
	private final int PORT;
	
	
	public UDPClient(int p) {
		PORT = p;
		try {
			GROUP = InetAddress.getByName("localhost");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		media = new Media((byte)123, 406, 24883254);
		
	}
	
	@Override
	public void run() {
		try {
			socket = new DatagramSocket();
			byte[] rpBuffer = new byte[71680];
			
			while (true) {
                try {

                    // Get the mediaRequestMessage.
                    byte[] msgOut = MessageUtility.getMediaRequestMessage(media.getImgID(),media.getExpectingPacketNumber());

                    // Build and send the packet.
                    DatagramPacket packet = new DatagramPacket(msgOut, msgOut.length, GROUP, PORT);
                    socket.send(packet);

                    // Wait for the data to come back.
                    DatagramPacket rp = new DatagramPacket(rpBuffer, rpBuffer.length);
                    socket.receive(rp);
                    byte[] msgIn = rp.getData();

                    // Process the message
                    if(! MessageUtility.parseMediaResponse(msgIn,media) ){
                        System.out.println("Error wrong headers media request response");
                        break;
                    }

                    if(media.isReady()){
                        break;
                    }
                } catch (SocketTimeoutException e) {
                	System.out.println("Timeout Reached, resending...");

                } catch (IOException e) {
                	System.out.println("IO Error on send");
                    break;
                }
            }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    class MediaResponse{
        int imgLength;
        byte imgID;
        byte packetCount;
        MediaResponse(){

        }
    }
	
}
