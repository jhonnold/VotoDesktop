package gui;

/**
 * This class contains the GUI methods
 */
import java.net.*;

import javafx.application.*;
import javafx.scene.image.Image;
import javafx.stage.*;
import session.Session;

/**
 * The Main class in the project
 * 
 * @author Nic
 *
 */
public class VotoDesktopFX extends Application {

	// Fields
	private static Session s = new Session("test");
	protected static String IP;
	protected static Stage primary;
	protected static HostScene host;
	protected static LaunchScene launch;
	private static VotoMenuBar menu = new VotoMenuBar(s);

	public static Level outputMode = Level.ALL;

	public enum Level {
		ALL(4), HIGH(3), MED(2), LOW(1), NONE(0);

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

		primary.getIcons().add(new Image("file:voto_icon.png"));

		// Add IP address
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ue) {
			System.exit(1);
		}

		launch = new LaunchScene(600, 200, menu);
		host = new HostScene(s, 600, 200, menu);

		primaryStage.setScene(launch);
		primaryStage.setResizable(false);
		primaryStage.setTitle("VOTO - " + IP);
		primaryStage.show();

		// Closes program and stops Session
		primaryStage.setOnCloseRequest(e -> exitProgram());
	}

	/**
	 * Quit application
	 */
	protected static void exitProgram() {
		Platform.exit();
		try {
			s.stop();
		} catch (IllegalArgumentException e) {
		}
		System.exit(0);
	}

	/**
	 * Host GUI displays IP address, allows user to open pictures, displays
	 * pictures, and lets the user select the correct answer for each picture
	 */
	protected static void hostGUI() {

		if (host.start() != null) {

			try {
				s.start();
			} catch (SocketException se) {
				System.out.println("Couldn't lock to the session socket!");
				host.reset();
				return;
			}

			primary.setScene(host);
			primary.show();
		} else {
			VotoDesktopFX.launch.reinstallMenuBar(menu);
		}
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
}