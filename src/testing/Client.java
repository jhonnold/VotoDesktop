
package testing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client implements Runnable {
	
	JFrame f;
	
	public static void main(String[] args) {
		
		new Thread(new Client()).start();
		
	}
	
	public Client() {
		//f = new JFrame();
		//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//f.setVisible(true);
	}
	
	public void run() {
		
		UDPClient UDP = new UDPClient(9876);
		Thread client = new Thread(UDP);
		client.start();
		
	}
	
}
