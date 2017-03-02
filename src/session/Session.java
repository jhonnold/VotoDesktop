package session;

import java.util.ArrayList;

public class Session {
	
	public String ID = "test server";
	
	private ArrayList<String> clientList = new ArrayList<String>();
	
	public void addClient(String client) {
		if (!clientList.contains(client)) {
			System.out.println("New client, added as: " + client);
			clientList.add(client);
		} else {
			System.out.println("Client already exists, not adding");
		}
	}

	public int getCurrentImageID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCurrentImagePacketCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isConnectedClient(String string) {
		// TODO Auto-generated method stub
		return true;
	}

	public byte[] getImagePacket(int imageID, int packetNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
