package gui;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import controller.NetworkController;
import controller.SessionController;

public class VotoDesktop implements Runnable, ActionListener {
	
	private JFrame f;
	private Timer t = new Timer(100, this);
	private JTextArea console = new JTextArea(24, 80);
	private ConsoleOutput output = new ConsoleOutput(console, "Voto-Desktop");
	
	NetworkController nc = null;
	SessionController sc;
	
	public VotoDesktop() {
		
		f = new JFrame();
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		console.setEditable(false);
		f.add(new JScrollPane(console));
		System.setOut(new PrintStream(output));
		
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
		nc.run();		
	}
	
	public static void main(String[] args) {
		new Thread(new VotoDesktop()).start();
	}
}
