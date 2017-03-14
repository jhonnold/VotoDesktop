package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.SocketException;

import javax.swing.*;

import controller.Controller;

public class VotoDesktop implements Runnable, ActionListener {
	
	private JFrame f;
	private JButton hostButton, joinButton;
	private JPanel startPanel;
	private Timer t;
	private JFileChooser fileChooser;
	//private JTextArea console = new JTextArea(24, 80);
	//private ConsoleOutput output = new ConsoleOutput(console, "Voto-Desktop");
	
	private Controller c;
	
	public VotoDesktop() {
		
		t = new Timer(100, this);
		
		f = new JFrame();
		f.setSize(200, 200);
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		startPanel = new JPanel(new GridLayout(3,1, 0, 25));
		f.add(startPanel);
		
		startPanel.add(new JLabel("VOTO"));
		
		startPanel.add(hostButton = new JButton("Host Session"));
		hostButton.addActionListener(this);
		
		startPanel.add(joinButton = new JButton("Join Session"));
		joinButton.addActionListener(this);
		
		
		//f.pack();
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		f.repaint();
		Toolkit.getDefaultToolkit().sync();
		
		if (e.getSource() == hostButton) {
			f.remove(startPanel);
			//System.setOut(new PrintStream(output));
			try {
				c.start();
			} catch (SocketException se) {
				se.printStackTrace();
			}
		}
		if (e.getSource() == joinButton) {
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// + System.getProperty("file.separator")+ "Pictures"));
			int result = fileChooser.showOpenDialog(startPanel);
		}
	}
	
	@Override
	public void run() {
		
		c = new Controller();
			
		//c.run();		
	}
	
	public static void main(String[] args) {
		new Thread(new VotoDesktop()).start();
	}
}
