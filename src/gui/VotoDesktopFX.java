package gui;
/**
 * This class contains the GUI methods
 */
import java.net.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import session.Session;


public class VotoDesktopFX extends Application {
	
	private Button hostButton, joinButton, setupButton;
	private Session s = new Session("test");
	protected static String IP;
	private VotoMenuBar menu = new VotoMenuBar(s, this);
	private BorderPane root;
	private Stage primary;
	
	
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


		//Instantiate elements
		hostButton = new Button("Host Session");
		hostButton.setOnAction(e -> hostGUI());
		
		joinButton = new Button("Join Session");
		joinButton.setOnAction(e -> joinGUI(primaryStage));
		
		setupButton = new Button("Setup Session");
		setupButton.setOnAction(e -> new SetupStage());
		
		//Add to stage
		root = new BorderPane();
		root.setTop(menu);
		
		// Create 3x1 Grid pane 100% for row, 33.33% for each column
		GridPane gp = new GridPane();
		ColumnConstraints c = new ColumnConstraints();
		RowConstraints r = new RowConstraints();
		c.setPercentWidth(33.33);
		r.setPercentHeight(100);
		gp.getColumnConstraints().addAll(c, c, c);
		gp.getRowConstraints().add(r);
		
		// Add button to each HBox centered
		HBox b1 = new HBox();
		b1.setAlignment(Pos.CENTER);
		b1.getChildren().add(hostButton);
		HBox b2 = new HBox();
		b2.setAlignment(Pos.CENTER);
		b2.getChildren().add(joinButton);
		HBox b3 = new HBox();
		b3.setAlignment(Pos.CENTER);
		b3.getChildren().add(setupButton);
		
		// Add the HBox's to the gridpane
		gp.add(b1, 0, 0);
		gp.add(b2, 1, 0);
		gp.add(b3, 2, 0);
		root.setCenter(gp);
		
	    Scene scene = new Scene(root, 600, 200);
	    primaryStage.setScene(scene);
	    primaryStage.setResizable(false);
	    primaryStage.setTitle("VOTO - " + IP);
	    primaryStage.show();
	    
	  //Closes program and stops Session
	    primaryStage.setOnCloseRequest(e -> exitProgram());
	}
	
	protected void exitProgram() {
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
	protected void hostGUI() {
		primary.setScene(new HostScene(s, 600, 200, menu));
		primary.show();
		
		//Start session
		try {
			s.start();
		} 
		catch (SocketException se) {
			se.printStackTrace();
		}
	}
	
	
	// Join GUI
	private void joinGUI(Stage p) {
		p.hide();
		p.setScene(new VoteScene(600, 400));
		p.show();
	}
	
	//main method
	public static void main(String[] args) {
		Application.launch(args);
	}
}