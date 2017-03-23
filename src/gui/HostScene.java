package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import session.*;

public class HostScene extends Scene {

	private static BorderPane rootHost = new BorderPane();
	private ScrollPane picPane;
	private HBox pics;
	private FlowPane centerPic;
	private FileChooser fc;
	private Session s;
	private File file;
	private Button next;
	private int picIndex = 0;
	private TextArea consoleOutput = new TextArea();
	
	private MenuBar menu;
	private Menu fileMenu, sessionMenu, windowMenu;
	private MenuItem newItem, openItem, saveItem, exitItem;
	private MenuItem nextItem;
	private MenuItem consoleItem, clientsItem, graphItem, connectionItem;
	
	
	public HostScene(Session se, double width, double height) {
		super(rootHost, width, height);

		s = se;

		//Instantiate new GUI elements
		picPane = new ScrollPane();
		//picPane.setMinHeight(200);
		pics = new HBox();
		picPane.setContent(pics);
		
		//Button open = new Button("Open File");
		//open.setOnAction(e -> openFile());	//Add action listener to open file chooser
		
		//next = new Button("Begin");
		//next.setOnAction(e -> nextPic());
		
		//Add IP address
		/*try {
			hostGrid.add(new Label(InetAddress.getLocalHost().getHostAddress()), 0, 0);
		}
		catch (UnknownHostException ue) {
			System.exit(1);
		}*/
		
		//Add elements to stage
		/*hostGrid.add(open, 0, 1);
		hostGrid.add(consoleOutput, 0, 3);
		hostGrid.add(next, 0, 2);*/
		
		/*rootHost.setLeft(hostGrid);
		rootHost.setCenter(centerPic);*/
		createMenu();
		rootHost.setTop(menu);
		rootHost.setCenter(picPane);
	}


	/**
	 * Open picture from file chooser to host pane
	 */
	private void  openFile() {
		
		//Instantiate
		fc = new FileChooser();
		//fc.setInitialDirectory(new File(System.getProperty("user.home") + ".voto-desktop"));
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("VOTO files (*.voto)", "*.voto");
        fc.getExtensionFilters().add(extFilter);
		file = fc.showOpenDialog(null);
		Scanner txtScan = null;
		String filePath = null;
		String answer = null;
		
		//Load picture if one was selected
		if (file != null) {
			if (file.getPath().endsWith(".voto")) {
				try {
					txtScan = new Scanner(file);
					while (txtScan.hasNext()) {
						filePath = txtScan.nextLine();
						answer = txtScan.nextLine();
						addPic(filePath);
						s.setCurrentQuestion(filePath, answer);
						//s.setCurrentQuestion(file.getPath(), "A");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				addPic(file.getAbsolutePath());
		}		
	}

	private void addPic(String filepath) {
		ImageView iView = null;
		File currentFile = new File(filepath);
		try {
			//Instantiate image view from picture
			BufferedImage bImage = ImageIO.read(currentFile);
			Image image = SwingFXUtils.toFXImage(bImage, null);
			iView = new ImageView();
			iView.setImage(image);

		} catch (IOException e) {
			System.exit(1);
		}

		addImgToSP(iView);


		try {
			ArrayList<byte[]> qImg = s.loadImage(currentFile.getPath());
			//s.currentQuestion = new Question(s, qImg, (int)(Math.random() * 20));
			s.setCurrentQuestion(file.getPath(), "A");
			System.out.println("Loaded image size: " + s.getCurrentImageSize());
			System.out.println("Packet count: " + s.getCurrentImagePacketCount());
		} catch (Exception e) {

		}

	}

	private void addImgToSP(ImageView iViewPrev) {
		iViewPrev.setPreserveRatio(true);
		iViewPrev.setFitHeight(165);
		pics.getChildren().add(picIndex , iViewPrev);
		picIndex++;
	}
	
	
	private void nextPic() {
		if (s == null) {
			try {

				s = new Session("test");
				s.start();
				next.setText("Next");
			}
			catch (SocketException se) {
				se.printStackTrace();
			}
		}
	}
	
	public void createMenu() {
		menu = new MenuBar();
		
		// File menu
		fileMenu = new Menu("File");
		newItem = new MenuItem("New");
		openItem = new MenuItem("Open");
		saveItem = new MenuItem("Save");
		exitItem = new MenuItem("Exit");
		fileMenu.getItems().addAll(newItem, openItem, saveItem, exitItem);
		
		// Session menu
		sessionMenu = new Menu("Session");
		nextItem = new MenuItem("Next");
		sessionMenu.getItems().addAll(nextItem);
		
		// Window menu
		windowMenu = new Menu("Window");
		consoleItem = new MenuItem("Console");
		clientsItem = new MenuItem("Clients");
		graphItem = new MenuItem("Graph");
		connectionItem = new MenuItem("Connection Info");
		windowMenu.getItems().addAll(consoleItem, clientsItem, graphItem, connectionItem);
		
		menu.getMenus().addAll(fileMenu, sessionMenu, windowMenu);
		
		// Set menu item actions
		//newItem.setOnAction(e -> new ConsoleStage());
		openItem.setOnAction(e -> openFile());
		//saveItem.setOnAction(e -> new ConsoleStage());
		//exitItem.setOnAction(e -> new ConsoleStage());
		//nextItem.setOnAction(e -> new ConsoleStage());
		consoleItem.setOnAction(e -> new ConsoleStage());
		clientsItem.setOnAction(e -> new ClientStage(s));
		//graphItem.setOnAction(e -> new GraphStage());
		//connectionItem.setOnAction(e -> new ConnectionStage());
	}
}