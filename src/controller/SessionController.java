package controller;

import java.util.ArrayList;

import session.Session;

public class SessionController {
	
	public NetworkController networkController;
	public Session session;
	
	/**
	 * Handshake request received 
	 * @param kwargs - [1-ip 2-port 3-clientid]
	 */
	public void handshakeRequest(ArrayList<String> kwargs) {
		
		//add handshake to session
		
		kwargs.set(3, "" + session.ID);
		networkController.reply(kwargs);
	}
	
	/**
	 * Vote was received
	 * @param kwargs - [1-ip 2-port 3-clientid 4-vote]
	 */
	public void vote(ArrayList<String> kwargs) {
		
		//parse and save vote to session
		
		kwargs.set(3, "" + session.ID);
		networkController.reply(kwargs);
	}
	
}
