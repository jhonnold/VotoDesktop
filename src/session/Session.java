package session;

import java.util.ArrayList;

import testing.Server;

public class Session {
	
	public int ID = (int)(Math.random() * 100);
	
	private ArrayList<String> clientList = new ArrayList<String>();
	
	public void addClient(String client) {
		if (!clientList.contains(client)) {
			System.out.println("New client, added as: " + client);
			clientList.add(client);
		}
	}
}
