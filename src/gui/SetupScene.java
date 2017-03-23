package gui;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class SetupScene extends Scene {
	
	private static BorderPane rootSetup = new BorderPane();
	private FileChooser fc;
	private File file;
	private BufferedWriter output;
	
	private ScrollPane picPane;
	private HBox pics;
	private FlowPane centerPic;
	private int picIndex = 0;
	private GridPane setupGrid;
	private Button open, done;
	private VotoDesktopFX voto;
	private Stage primaryStage;
	private String fileName;
	
	public SetupScene(double width, double height, VotoDesktopFX v, Stage p) {
		super(rootSetup, width, height); 
		voto = v;
		primaryStage = p;
		
		picPane = new ScrollPane();
		picPane.setMinHeight(120);
		pics = new HBox();
		picPane.setContent(pics);
		centerPic = new FlowPane();
		setupGrid = new GridPane();
		
		
		open = new Button("Open File");
		open.setOnAction(e -> openFile());	//Add action listener to open file chooser
		done = new Button("Finish");
		done.setOnAction(e -> close());
		done.setPrefSize(67, 25);
		
		//Add elements to stage
		setupGrid.add(open, 0, 0);
		setupGrid.add(done, 0, 1);
				
		rootSetup.setLeft(setupGrid);
		rootSetup.setCenter(centerPic);
		rootSetup.setBottom(picPane);
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setContentText("Please enter session name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    fileName = result.get();
		}
		
		try {
			File outFile = new File(fileName+".voto");
			output = new BufferedWriter(new FileWriter(outFile));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			try {
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		});
	}
	
	private void  openFile() {
		
		//Instantiate
		fc = new FileChooser();
		file = fc.showOpenDialog(null);
		String filePath = null;
		
		try {				
			//Load picture if one was selected
			if (file != null) {
				filePath = file.getAbsolutePath();
				addPic(filePath);
				output.write(filePath);
				output.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
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

		//open image to center
			iView.setFitHeight(410);
			iView.setFitWidth(400);
			centerPic.getChildren().add(iView);
			answerStage();
	}
	
	//GUI for setting answer for image
	private void answerStage() {
			
		//Instantiate new elements
		VBox ansPane = new VBox();
		ansPane.setPadding(new Insets(0, 7, 7, 7));
		ansPane.getChildren().add(new Label("Set Correct Answer"));
		//ansPane.hgap(10);
		ansPane.setAlignment(Pos.CENTER);
		rootSetup.setRight(ansPane);
			
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
					    try {
							output.write(button.getText());
							output.newLine();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					    rootSetup.getChildren().remove(ansPane);
						    
						//Add image to scrollpane
						if (!centerPic.getChildren().isEmpty()) {
						 	addImgToSP((ImageView) centerPic.getChildren().remove(0));
						}
				}
			});
		}
	}
	
	private void addImgToSP(ImageView iViewPrev) {
		iViewPrev.setFitHeight(100);
		iViewPrev.setFitWidth(100);
		pics.getChildren().add(picIndex  , iViewPrev);
		picIndex++;
	}
	
	private void close() {
		try {
			output.flush();
			output.close();
			voto.start(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

