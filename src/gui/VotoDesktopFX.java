package gui;

import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javafx.application.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.geometry.*;
import session.Question;
import session.Session;


public class VotoDesktopFX extends Application
						   implements Runnable, ActionListener {
	
	private ArrayList<String> picList;
	private Stage hostStage;
	private Button hostButton, joinButton, stopButton;
	private GridPane hostGrid;
	private BorderPane rootHost;
	private ScrollPane picPane;
	private FileChooser fc;
	private VBox pics;
	private Session s = new Session();
	
	//Start GUI
	@Override
	public void start(Stage primaryStage) {
		
		//Instantiate elements
		hostButton = new Button("Host Session");
		hostButton.setOnAction(e -> hostGUI(primaryStage));
		joinButton = new Button("Join Session");
		//joinButton.setOnAction(e -> joinGUI());
		
		
		//Add to stage
		GridPane startPane = new GridPane();
		startPane.setPadding(new Insets(15, 25, 25,45));
		startPane.setVgap(15);
	    
	    startPane.add(new Label("VOTO"), 0, 0);
		startPane.add(hostButton, 0, 1);	     
	    startPane.add(joinButton, 0, 2);
	     
	    Scene scene = new Scene(startPane, 175, 150);
	    primaryStage.setScene(scene);
	    primaryStage.setResizable(false);
	    primaryStage.show();
	}
	
	//Host GUI
	private void hostGUI(Stage p) {
		p.close();
		
		//Instantiate new GUI elements
		picList = new ArrayList<>();
		rootHost = new BorderPane();
		picPane = new ScrollPane();
		pics = new VBox();
		picPane.setContent(pics);
		
		Button open = new Button("Open File");
		
		open.setOnAction(e -> openFile());
		hostGrid = new GridPane();
		
		//Add IP address
		try {
			hostGrid.add(new Label(InetAddress.getLocalHost().getHostAddress()), 0, 0);
		}
		catch (UnknownHostException ue) {
			System.exit(1);
		}
		
		//Add elements to stage
		hostGrid.add(open, 0, 1);
		
		rootHost.setCenter(hostGrid);
		rootHost.setRight(picPane);
		
		Scene scene = new Scene(rootHost, 600, 400);
		hostStage = new Stage();
		hostStage.setScene(scene);
		hostStage.show();
		
		//Start session
		try {
			s.start();
		} 
		catch (SocketException se) {
			se.printStackTrace();
		}
	
		//Closes program and stops Session
		hostStage.setOnCloseRequest(e -> {
			Platform.exit();
			s.stop();
			System.exit(0);
		});
	}
	
	//Open picture from file chooser to host pane
	private void  openFile() {
		fc = new FileChooser();
		File file = fc.showOpenDialog(null);
		ImageView iView = null;
		if (file != null) {
			try {
				BufferedImage bImage = ImageIO.read(file);
				Image image = SwingFXUtils.toFXImage(bImage, null);
				iView = new ImageView();
				iView.setImage(image);
			} catch (IOException e) {
				System.exit(1);
			}
			iView.setFitHeight(100);
			iView.setFitWidth(100);
			pics.getChildren().add(iView);
			
			try {
				ArrayList<byte[]> qImg = s.loadImage(file.getPath());
				s.currentQuestion = new Question(s, qImg, 123);
				System.out.println("Loaded image size: " + s.getCurrentImageSize());
			} catch (Exception e) {
				
			}
			picList.add(file.getPath());
				
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Toolkit.getDefaultToolkit().sync();
		try {
			s.start();
		} 
		catch (SocketException se) {
			se.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		s = new Session();		
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}