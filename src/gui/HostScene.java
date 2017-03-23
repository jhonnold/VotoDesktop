package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	private GridPane hostGrid;
	private FileChooser fc;
	private Session s;
	private File file;
	private Button next;
	private int picIndex = 0;

	public HostScene(Session se, double width, double height) {
		super(rootHost, width, height);

		s = se;

		//Instantiate new GUI elements
		picPane = new ScrollPane();
		picPane.setMinHeight(120);
		pics = new HBox();
		picPane.setContent(pics);
		centerPic = new FlowPane();
		
		Button open = new Button("Open File");
		open.setOnAction(e -> openFile());	//Add action listener to open file chooser
		hostGrid = new GridPane();
		
		next = new Button("Begin");
		next.setOnAction(e -> nextPic());
		
		//Add IP address
		try {
			hostGrid.add(new Label(InetAddress.getLocalHost().getHostAddress()), 0, 0);
		}
		catch (UnknownHostException ue) {
			System.exit(1);
		}
		
		//Add elements to stage
		hostGrid.add(open, 0, 1);
		hostGrid.add(next, 0, 2);
		
		rootHost.setLeft(hostGrid);
		rootHost.setCenter(centerPic);
		rootHost.setBottom(picPane);
	}


	/**
	 * Open picture from file chooser to host pane
	 */
	private void  openFile() {
		
		//Instantiate
		fc = new FileChooser();
		file = fc.showOpenDialog(null);
		Scanner txtScan = null;
		String filePath = null;
		
		//Load picture if one was selected
		if (file != null) {
			if (file.getPath().endsWith(".txt")) {
				try {
					txtScan = new Scanner(file);
					while (txtScan.hasNext()) {
						addPic(filePath = txtScan.nextLine());
						s.setCurrentQuestion(filePath, txtScan.nextLine());
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


	//GUI for setting answer for image
	private void answerStage() {
		
		//Instantiate new elements
		VBox ansPane = new VBox();
		ansPane.setPadding(new Insets(0, 7, 7, 7));
		ansPane.getChildren().add(new Label("Set Correct Answer"));
		//ansPane.hgap(10);
		ansPane.setAlignment(Pos.CENTER);
		rootHost.setRight(ansPane);
			
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
					    
					    //Set correct answer
					    s.getCurrentQuestion().setAnswer(button.getText());
					    rootHost.getChildren().remove(ansPane);
					    
					    //Add image to scrollpane
					    if (!centerPic.getChildren().isEmpty()) {
					    	addImgToSP((ImageView) centerPic.getChildren().remove(0));
					    }
				}
			});
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


		//Add previous image view to scrollpane 
		/*if (!centerPic.getChildren().isEmpty()) {
				ImageView iViewPrev = (ImageView) centerPic.getChildren().remove(0);
				iViewPrev.setFitHeight(100);
				iViewPrev.setFitWidth(100);
				pics.getChildren().add(iViewPrev);
			}*/

		//open image to center
		if (!file.getPath().endsWith(".txt")) {
			iView.setFitHeight(410);
			iView.setFitWidth(400);
			centerPic.getChildren().add(iView);
			answerStage();
		}
		else {
			addImgToSP(iView);
		}

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
		iViewPrev.setFitHeight(100);
		iViewPrev.setFitWidth(100);
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
}