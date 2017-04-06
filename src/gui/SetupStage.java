package gui;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * The stage for creating the .voto files
 * @author Nic
 *
 */
public class SetupStage extends Stage{
	
	private BorderPane rootSetup;
	private FileChooser fc;
	private File file, outFile;
	private BufferedWriter output;
	
	private ScrollPane picPane;
	private HBox pics;
	private int picIndex = 0;
	private String fileName;
	Button[] ansButton;
	
	private Menu fileMenu;
	private MenuBar menuBar;
	private MenuItem openItem, saveItem, exitItem;
	
	/**
	 * Constructor for the setup stage
	 */
	public SetupStage() {
		super(); 

		rootSetup = new BorderPane();
		rootSetup.setPrefHeight(200);
		rootSetup.setPrefWidth(600);
		
		picPane = new ScrollPane();
		picPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		pics = new HBox();
		picPane.setContent(pics);
		
		
		answerPane();
		addMenu();		//centerPic = new FlowPane();
		rootSetup.setCenter(picPane);
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText(null);
		dialog.setTitle("New Session");
		dialog.setContentText("Please enter session name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent() && result.get().trim().length() > 0){
			fileName = result.get();
			try {
				outFile = new File(fileName+".voto");
				output = new BufferedWriter(new FileWriter(outFile));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// Our standard size
			Scene scene = new Scene(rootSetup, 600, 200);
			setScene(scene);
			setResizable(false);
			setTitle("Choose Picture");
			show();
		}
		
		//Exit confirmation
		setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  Alert alert = new Alert(AlertType.CONFIRMATION);
	        	  alert.setContentText("Do you want to exit without saving?");
	        	  Optional<ButtonType> result = alert.showAndWait();
	        	  if (result.get() == ButtonType.OK){
	        		  try {
	        			  output.close();
	        		  } catch (IOException e) {
	        			  e.printStackTrace();
	        		  }
	        		  outFile.delete();
	        	  } else {
	        		  we.consume();
	        		  alert.close();
	        	  }	  
	          }	
	      });
	}
	
	/**
	 * Loads specified file through a filechooser
	 */
	private void openFile() {
		
		//Instantiate
		fc = new FileChooser();
		fc.setInitialDirectory(new File("./"));
		file = fc.showOpenDialog(null);
		String filePath = null;
		
		try {				
			//Load picture if one was selected
			if (file != null) {
				filePath = file.getAbsolutePath();
				addPic(filePath);
				output.write(filePath);
				output.newLine();
								
				//enable buttons
				for (Button b : ansButton) {
					b.setDisable(false);
				}
				openItem.setDisable(true);
				saveItem.setDisable(true);
				setTitle("Set Correct Answer");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts image to ImageView and loads into HBox
	 * @param filepath The image filepath
	 */
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
	
	/**
	 * The Answer Pane that allows you to select the correct answer
	 */
	private void answerPane() {
			
		//Instantiate new elements
		VBox ansPane = new VBox();
		ansPane.setPadding(new Insets(0, 7, 7, 7));
		ansPane.getChildren().add(new Label("Set Correct Answer"));
		//ansPane.hgap(10);
		ansPane.setAlignment(Pos.CENTER);
		rootSetup.setLeft(ansPane);
			
		//Create buttons
		ansButton = new Button[5];
		for (int index = 0; index < 5; index++) {
			ansButton[index] = new Button(Character.toString((char)(0x0041+index)));
			ansButton[index].setMinSize(55, 25);
			ansButton[index].setDisable(true);
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
					    
					    //disable buttons
					    for (Button b : ansButton) {
							b.setDisable(true);
						}
					    openItem.setDisable(false);
					    saveItem.setDisable(false);
					    setTitle("Choose Picture");
				}
			});
		}
	}
	
	/**
	 * Basic menubar for this stage
	 */
	private void addMenu() {
		menuBar = new MenuBar();
		fileMenu = new Menu("File");
		fileMenu = new Menu("File");
		openItem = new MenuItem("Open");
		saveItem = new MenuItem("Save");
		exitItem = new MenuItem("Exit");
		fileMenu.getItems().addAll(openItem, saveItem, exitItem);
		
		menuBar.getMenus().addAll(fileMenu);
		rootSetup.setTop(menuBar);
		
		// Set menu item actions
		openItem.setOnAction(e -> openFile());
		saveItem.setOnAction(e -> saveFile());
		exitItem.setOnAction(e -> close());
	}
	
	/**
	 * Add imageview to the Hbox
	 * @param iViewPrev The imageview to be added
	 */
	private void addImgToSP(ImageView iViewPrev) {
		iViewPrev.setPreserveRatio(true);
		iViewPrev.setFitHeight(rootSetup.getHeight() - menuBar.getHeight() - 2);
		pics.getChildren().add(picIndex, iViewPrev);
		picIndex++;
	}
	
	/**
	 * Saves the current session voto file
	 */
	private void saveFile() {
		try {
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}