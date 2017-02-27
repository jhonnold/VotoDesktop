package controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.ArrayList;

import networking.NetworkHandler;
import session.Session;

public class Controller {
	
	
	private NetworkHandler network;
	private Session session = new Session();
	
	private boolean running = false;
	
	public Controller() {}
	
	/**
	 * Starts the network socket to start accepting
	 * packets from clients
	 * @throws SocketException -- if the port 9876 is in use
	 */
	public void start() throws SocketException {
		try {
			network = new NetworkHandler(this);
		} catch (SocketException e) {
			throw new SocketException("Failed to start the listening socket.");
		}
		
		new Thread(network).start();
		running = true;
	}
	
	/**
	 * Stops the network socket from accepting packets
	 * from clients
	 * @throws IllegalArgumentException -- if nothing is running
	 */
	public void stop() throws IllegalArgumentException {
		if (!running) {
			throw new IllegalArgumentException("Server is not currently active!");
		}
		
		network.close();
	}
	
	
	/**
	 * Handshake request received 
	 * @param kwargs - [1-ip 2-port 3-clientid]
	 */
	public void handshakeRequest(ArrayList<String> kwargs) {
		System.out.println("Parsed as a handshake");
		
		if (kwargs.size() <= 2) {
			System.out.println("Not formatted properly, replying with error");
			network.replyError(kwargs, "ERROR handshakeRequest_[id]");
		} else if (kwargs.size() == 3) {
			session.addClient(kwargs.get(1));
			kwargs.add("" + session.ID);
			network.reply(kwargs);
		} else {
			session.addClient(kwargs.size() == 3 ? kwargs.get(1) : kwargs.get(3));
			kwargs.set(3, "" + session.ID);
			network.reply(kwargs);
		}
	}
	
	/**
	 * Vote was received
	 * @param kwargs - [1-ip 2-port 3-clientid 4-vote]
	 */
	public void vote(ArrayList<String> kwargs) {
		
		//parse and save vote to session
		
		kwargs.set(3, "" + session.ID);
		network.reply(kwargs);
	}
	
	public void parseNetworkCommand(ArrayList<String> kwargs) throws NoSuchMethodException {
		
		try {
			Method method = this.getClass().getMethod(kwargs.get(0), ArrayList.class); 
			method.invoke(this, kwargs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("Gibberish");
		}
		
	}
	
}
