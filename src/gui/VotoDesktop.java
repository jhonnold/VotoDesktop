package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import controller.NetworkController;
import controller.SessionController;

public class VotoDesktop implements Runnable, ActionListener {
	
	private JFrame f;
	private Timer t = new Timer(100, this);
	private ConsoleOutput out = new ConsoleOutput();
	
	NetworkController nc = null;
	SessionController sc;
	
	public VotoDesktop() {
		
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//pane.setPreferredSize(new Dimension(800, 600));
		
		f.add(out);
		f.pack();
		
		f.setVisible(true);
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		f.repaint();
		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override
	public void run() {
		
		try {
			nc = new NetworkController();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		sc = new SessionController();
		
		nc.addSessionController(sc);
		sc.addNetworkController(nc);
		
		nc.addOutput(out);
		//sc.addOutput(out);
		
		nc.run();		
	}
	
	public static void main(String[] args) {
		new Thread(new VotoDesktop()).start();
	}
}
