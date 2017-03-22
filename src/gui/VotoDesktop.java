package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import session.Session;

public class VotoDesktop implements Runnable, ActionListener {
	
	private JFrame f;
	private JButton hostButton, joinButton, openButton, connectButton;
	private JPanel startPanel, hostPanel;
	private Timer t;
	private JFileChooser fileChooser;
	private JLabel ipLabel;
	private JTextField ipField;
	//private JTextArea console = new JTextArea(24, 80);
	//private ConsoleOutput output = new ConsoleOutput(console, "Voto-Desktop");
	
	private Controller c;
	private Session s;
	
	public VotoDesktop() {
		
		startGUI();
		
		t = new Timer(100, this);
		
		
		t.start();
	}
	
	private void startGUI() {
		f = new JFrame();
		f.setSize(200, 200);
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//StartPanel setup
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
	}
	
	private void hostGUI(){
		f.setVisible(false);
		f.remove(startPanel);
						
		hostPanel = new JPanel(new BorderLayout());
		try {
			ipLabel = new JLabel(InetAddress.getLocalHost().getHostAddress());
		}
		catch (UnknownHostException ue) {
			System.exit(1);
		}
		
		openButton = new JButton("Open");
		openButton.addActionListener(this);
		
		hostPanel.add(ipLabel, BorderLayout.NORTH);
		hostPanel.add(openButton, BorderLayout.SOUTH);
		
		f.add(hostPanel);
		f.setTitle("Host Session");
		f.setSize(600, 400);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.repaint();
	}
	
	private void joinGUI() {
		f.setVisible(false);
		f.remove(startPanel);
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		
		ipField = new JTextField("Enter IP");
		ipField.setPreferredSize(new Dimension(175, 25));
		
		f.add(ipField);
		f.add(connectButton);
		
		f.setTitle("Join Session");
		f.setSize(600, 400);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		f.repaint();
		Toolkit.getDefaultToolkit().sync();
		
		
		//HostButton press action
		if (e.getSource() == hostButton) {
			
			hostGUI();
			
			//System.setOut(new PrintStream(output));
			try {
				//c.start();
				s.start();
			} 
			catch (SocketException se) {
				se.printStackTrace();
			}
		}
		
		//JoinButton (FileChooser)
		if (e.getSource() == joinButton) {
			joinGUI();
		}
		
		if (e.getSource() == openButton) {
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// + System.getProperty("file.separator")+ "Pictures"));
			int result = fileChooser.showOpenDialog(startPanel);
		}
		
		if (e.getSource() == connectButton) {
			
		}
	}
	
	@Override
	public void run() {
		
		//s = new Session();		
	}
	
	public static void main(String[] args) {
		new Thread(new VotoDesktop()).start();
	}
}
