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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import session.*;

public class HostScene extends Scene {

	private static BorderPane rootHost = new BorderPane();
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
	
	public HostScene(Session se, double width, double height, VotoMenuBar mb) {
		super(rootHost, width, height);
		
		questions = new ArrayList<>();
		
		this.mb = mb;
		mb.setOpenFile(e -> openFile());
		mb.setNext(e -> nextQuestion());
		
		s = se;

		// Instantiate new GUI elements
		picPane = new ScrollPane();
		//picPane.setMinHeight(200);
		pics = new HBox();
		picPane.setContent(pics);
		rootHost.setTop(mb);
		rootHost.setCenter(picPane);
	}

	/**
	 * Open picture from file chooser to host pane
	 */
	private void openFile() {

		// Instantiate
		fc = new FileChooser();
		//fc.setInitialDirectory(new File(System.getProperty("user.home") + ".voto-desktop"));
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("VOTO files (*.voto)", "*.voto");
        fc.getExtensionFilters().add(extFilter);
		file = fc.showOpenDialog(null);
		Scanner txtScan = null;
		String filePath = null;
		String answer = null;
		if (file != null) {
			if (file.getPath().endsWith(".voto")) {
				try {
					txtScan = new Scanner(file);
					while (txtScan.hasNext()) {
						filePath = txtScan.nextLine();
						answer = txtScan.nextLine();
						addPic(filePath);
						questions.add(filePath);
						questions.add(answer);
						//s.setCurrentQuestion(file.getPath(), "A");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
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

	}

	private void addImgToSP(ImageView iViewPrev) {
		iViewPrev.setPreserveRatio(true);
		iViewPrev.setFitHeight(160);
		pics.getChildren().add(picIndex , iViewPrev);
		picIndex++;
	}
	
	private void nextQuestion() {
		s.setCurrentQuestion(questions.get(questionIndex), questions.get(++questionIndex) );
		questionIndex++;
	}
	
}