package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import session.*;

/**
 * The active session scene for voting and changing what the clients see
 * @author Nic / Josh
 *
 */
public class HostScene extends Scene {

	private static BorderPane rootHost = new BorderPane();
	private static Pane pane = new Pane();
	private ScrollPane picPane;
	private HBox pics;
	private FileChooser fc;
	private Session s;
	private File file;
	private Button next;
	private int picIndex = 0;
	private MenuBar mb;
	private ArrayList<String> questions;
	private int questionIndex = 0;

	/**
	 * The scene that will display the images and is essentially
	 * the active scene for the program
	 * @param se The active session for the scene
	 * @param width The width of the scene
	 * @param height The height of the scene
	 * @param mb The menubar to be passed in with the scene
	 */
	public HostScene(Session se, double width, double height, VotoMenuBar mb) {
		super(pane, width, height);
		
		rootHost.setPrefHeight(height);
		rootHost.setPrefWidth(width);
		
		questions = new ArrayList<>();
		this.mb = mb;

		mb.setOpenFile(e -> openFile());
		mb.setNext(e -> nextQuestion());

		s = se;

		// Instantiate new GUI elements
		picPane = new ScrollPane();
		picPane.setFitToHeight(true);
		// picPane.setMinHeight(200);
		pics = new HBox();
		pics.setPrefHeight(160);
		picPane.setContent(pics);

		openFile();

		rootHost.setTop(mb);
		rootHost.setCenter(picPane);
		
		pane.getChildren().add(rootHost);
		
		Button next = new Button("Next");
		next.setPrefHeight(25);
		next.setPrefWidth(50);
		next.setOnAction(e -> nextQuestion());
		next.setLayoutX(width - next.getPrefWidth() - 30);
		next.setLayoutY(height - next.getPrefHeight() - 30);
		
		pane.getChildren().add(next);
		
		for (int i = 0; i < questions.size(); i+=2) {
			addPic(questions.get(i));
		}
		
	}

	/**
	 * Open picture from file chooser to host pane
	 */
	private void openFile() {

		// Instantiate
		fc = new FileChooser();
		// fc.setInitialDirectory(new File(System.getProperty("user.home") + ".voto-desktop"));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("VOTO files (*.voto)", "*.voto");
		fc.getExtensionFilters().add(extFilter);
		file = fc.showOpenDialog(null);
		
		Scanner txtScan = null;
		String filePath = null;
		String answer = null;
		
		if (file != null && file.getPath().endsWith(".voto")) {
			try {
				txtScan = new Scanner(file);
				while (txtScan.hasNext()) {
					filePath = txtScan.nextLine();
					answer = txtScan.nextLine();
					//addPic(filePath);
					if (questionIndex == 0) {
						s.setCurrentQuestion(filePath, answer);
						questionIndex += 2;
						questions.add(filePath);
						questions.add(answer);
					} else {
						questions.add(filePath);
						questions.add(answer);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Opens picture and loads into an ImageView
	 * @param filepath The filepath of the image to be loaded
	 */
	private void addPic(String filepath) {
		ImageView iView = null;
		File currentFile = new File(filepath);
		try {
			// Instantiate image view from picture
			BufferedImage bImage = ImageIO.read(currentFile);
			Image image = SwingFXUtils.toFXImage(bImage, null);
			iView = new ImageView();
			iView.setImage(image);

		} catch (IOException e) {
			System.exit(1);
		}
		addImgToSP(iView);

	}

	/**
	 * Adds image to the HBox
	 * @param iViewPrev The image to be added as ImageView
	 */
	private void addImgToSP(ImageView iViewPrev) {
		iViewPrev.setPreserveRatio(true);
		iViewPrev.setFitHeight(160);
		pics.getChildren().add(picIndex, iViewPrev);
		picIndex++;
	}
	
	/**
	 * Moves onto the nextQuestion in the session
	 */
	private void nextQuestion() {
		s.setCurrentQuestion(questions.get(questionIndex), questions.get(++questionIndex));
		questionIndex++;
	}

}