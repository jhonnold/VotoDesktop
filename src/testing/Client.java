
package testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Test class that fakes android clients talking to the server
 */
public class Client implements Runnable {
	
	private DatagramSocket socket;
	private InetAddress SERVER;
	private int PORT;
	private String ID;
	
	private int imageID = -100, index;
	private MediaResponse mr = new MediaResponse();
	private Media m;
	
	private boolean voted = false;
	private int voteNum = 1;
	
	public Client(String IP, String ID, int index) {
		
		try {
			SERVER = InetAddress.getByName(IP);
			socket = new DatagramSocket();
			socket.setSoTimeout(1000);
			PORT = 9876;
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		this.ID = ID;
		this.index = index;
		
	}
	
	
	@Override
	public void run() {
		
		// Start the handshake
		byte[] handshakeRequest = MessageUtility.getHandshakeRequestMessage(ID);
		DatagramPacket out = new DatagramPacket(handshakeRequest, handshakeRequest.length, SERVER, PORT);
		
		try {
			socket.send(out);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		byte[] buffer = new byte[71680];
		DatagramPacket in = new DatagramPacket(buffer, buffer.length);
		
		try {
			socket.receive(in);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		// Start the media ping process
		byte[] mediaPing = MessageUtility.getMediaPingMessage();
		out = new DatagramPacket(mediaPing, mediaPing.length, SERVER, PORT);
		
		try {
			socket.send(out);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		while (true) {
			
			// Start the receive of media pings
			try {
				socket.receive(in);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				continue;
			}
			
			MessageUtility.parseMediaPing(in.getData() , mr);
			
			// If this is a new media, then start to load that
			if (mr.imgID != imageID) {
				m = new Media(mr.imgID, mr.packetCount, mr.imgLength, index);
				voted = false;
				while (!m.isReady()) {
					
					try {

	                    // Get the mediaRequestMessage.
	                    byte[] msgOut = MessageUtility.getMediaRequestMessage(m.getImgID(), m.getExpectingPacketNumber());

	                    // Build and send the packet.
	                    out = new DatagramPacket(msgOut, msgOut.length, SERVER, PORT);
	                    socket.send(out);

	                    // Wait for the data to come back.
	                    in = new DatagramPacket(buffer, buffer.length);
	                    socket.receive(in);
	                    byte[] msgIn = in.getData();

	                    // Process the message
	                    if(!MessageUtility.parseMediaResponse(msgIn, m) ){
	                        System.out.println("Error wrong headers media request response");
	                        break;
	                    }

	                } catch (SocketTimeoutException e) {
	                	System.out.println("Timeout Reached, resending...");
	                	continue;

	                } catch (IOException e) {
	                	System.out.println("IO Error on send");
	                }
				}
				
				imageID = mr.imgID;
				
			}
			
			// Wait 1 second between pings
			try {
				Thread.sleep(1000);
			} catch (Exception e) {};
			
			// send out the new ping
			mediaPing = MessageUtility.getMediaPingMessage();
			out = new DatagramPacket(mediaPing, mediaPing.length, SERVER, PORT);
			
			try {
				socket.send(out);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			if (!voted) {
				byte[] voteMsg = MessageUtility.getVoteMessage(ID, Character.toString(getVoteChar()), (byte)voteNum++);
				voted = true;
				
				out = new DatagramPacket(voteMsg, voteMsg.length, SERVER, PORT);
				try {
					socket.send(out);
				} catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
				try {
					socket.receive(in);
				} catch (Exception e) {}
			}
			
			
		}
		
	}
	
    class MediaResponse{
        int imgLength;
        byte imgID;
        int packetCount;
    }
    
	protected char getVoteChar() {
		String votes = "ABCDE";
		Random rnd = new Random();
		
		return votes.charAt(rnd.nextInt(5));
	}
	
}
