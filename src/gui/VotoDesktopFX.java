package gui;
/**
 * This class contains the GUI methods
 */
import java.net.*;

import javafx.application.*;
import javafx.stage.*;
import session.Session;

/**
 * The Main class in the project
 * @author Nic
 *
 */
public class VotoDesktopFX extends Application {
	
	private static Session s = new Session("test");
	protected static String IP;
	protected static Stage primary;
	protected static HostScene host;
	protected static LaunchScene launch;
	private static VotoMenuBar menu = new VotoMenuBar(s);
	
	/**
	 * Start GUI has host or join options
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.hide();
		
		primary = primaryStage;
		
		//Add IP address
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException ue) {
			System.exit(1);
		}
		
		launch = new LaunchScene(600, 200, menu);
		host = new HostScene(s, 600, 200, menu);
		
	    primaryStage.setScene(launch);
	    primaryStage.setResizable(false);
	    primaryStage.setTitle("VOTO - " + IP);
	    primaryStage.show();
	    
	  //Closes program and stops Session
	    primaryStage.setOnCloseRequest(e -> exitProgram());
	}
	
	/**
	 * Quit
	 */
	protected static void exitProgram() {
		Platform.exit();
		try {
			s.stop();
		}
		catch (IllegalArgumentException e) {}
		System.exit(0);
	}
	
	/**
	 * Host GUI displays IP address, allows user to open pictures, 
	 * displays pictures, and lets the user select the correct answer
	 * for each picture 
	 */
	protected static void hostGUI() {
		primary.setScene(host);
		primary.show();
		host.start();
		
		//Start session
		try {
			s.start();
		} 
		catch (SocketException se) {
			se.printStackTrace();
		}
	}
	
	//main method
	public static void main(String[] args) {
		Application.launch(args);
	}
}