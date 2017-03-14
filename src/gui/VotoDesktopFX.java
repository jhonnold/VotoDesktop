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

	private ArrayList<String> correctAnswer = new ArrayList<>();
	private Stage hostStage;
	private Button hostButton, joinButton, stopButton;
	private GridPane hostGrid;
	private BorderPane rootHost;
	private ScrollPane picPane;
	private FileChooser fc;
	private VBox pics;
	private Session s = new Session();
	private int index = 0;
	
	//Start GUI
	@Override
	public void start(Stage primaryStage) {

		//Instantiate elements
		hostButton = new Button("Host Session");
		hostButton.setOnAction(e -> hostGUI(primaryStage));
		joinButton = new Button("Join Session");
		//joinButton.setOnAction(e -> joinGUI());
		joinButton.setPrefSize(84, 25);
		
		
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
		rootHost = new BorderPane();
		picPane = new ScrollPane();
		picPane.setMinWidth(100);
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
				answerStage();
				
			} catch (IOException e) {
				System.exit(1);
			}
			iView.setFitHeight(100);
			iView.setFitWidth(100);
			pics.getChildren().add(iView);
			
			try {
				ArrayList<byte[]> qImg = s.loadImage(file.getPath());
				s.currentQuestion = new Question(s, qImg, 0);
				
			} catch (Exception e) {
				
			}
				
		}		
	}
	
	//GUI for setting answer for image
	public void answerStage() {
		
		//Instantiate new elements
		Stage ansStage = new Stage();
		FlowPane ansPane = new FlowPane();
		ansPane.setPadding(new Insets(0, 7, 7, 7));
		ansPane.getChildren().add(new Label("Set Correct Answer"));
		ansPane.setVgap(10);
		ansPane.setAlignment(Pos.CENTER);
		
		//Create buttons
		Button[] ansButton = new Button[5];
		for (int index = 0; index < 5; index++) {
			ansButton[index] = new Button(Character.toString((char)(0x0041+index)));
			ansButton[index].setMinSize(55, 25);
			ansPane.getChildren().add(ansButton[index]);
			
			//Add correct answer to list
			ansButton[index].setOnAction(e -> {
				Object source = e.getSource();
				if (source instanceof Button) {
				    Button button = (Button) source;
				    correctAnswer.add(button.getText());
				    ansStage.close();
				}
			});
		}
		
		Scene ansScene = new Scene(ansPane, 100, 210);
		ansStage.setScene(ansScene);
		ansStage.show();
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