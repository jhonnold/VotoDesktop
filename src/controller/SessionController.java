package controller;

import java.util.ArrayList;

import session.Session;

public class SessionController {
	
	private NetworkController networkController;
	private Session session = new Session();
	
	public void addNetworkController(NetworkController nc) {
		this.networkController = nc;
	}
	
	/**
	 * Handshake request received 
	 * @param kwargs - [1-ip 2-port 3-clientid]
	 */
	public void handshakeRequest(ArrayList<String> kwargs) {
		System.out.println("Parsed as a handshake");
		
		if (kwargs.size() <= 2) {
			System.out.println("Not formatted properly, replying with error");
			networkController.replyError(kwargs, "ERROR handshakeRequest_[id]");
		} else if (kwargs.size() == 3) {
			session.addClient(kwargs.get(1));
			kwargs.add("" + session.ID);
			networkController.reply(kwargs);
		} else {
			session.addClient(kwargs.size() == 3 ? kwargs.get(1) : kwargs.get(3));
			kwargs.set(3, "" + session.ID);
			networkController.reply(kwargs);
		}
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
