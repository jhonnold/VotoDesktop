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
	
	public static Level outputMode = Level.MED;
	
	public enum Level {
		ALL (4),
		HIGH (3),
		MED (2),
		LOW (1),
		NONE (0);
		
		public final int levelCode;
		
		Level(int level) {
			levelCode = level;
		}
		
		public boolean lt(Level l) {
			return levelCode <= l.levelCode;
		}
	}
	
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
				
		if (host.start() != null) {
			primary.setScene(host);
			primary.show();
			
		//Start session
		try {
			s.start();
		} 
		catch (SocketException se) {
			se.printStackTrace();
		}
		}
		else {
			VotoDesktopFX.launch.reinstallMenuBar(menu);
		}
	}
	
	//main method
	public static void main(String[] args) {
		Application.launch(args);
	}
}