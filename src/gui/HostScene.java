package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	
	public HostScene(Session se, double width, double height, MenuBar mb) {
		super(rootHost, width, height);
		
		this.mb = mb;
		s = se;

		//Instantiate new GUI elements
		picPane = new ScrollPane();
		picPane.setMinHeight(200);
		pics = new HBox();
		picPane.setContent(pics);
		
		Button next = new Button("Next");
		
		rootHost.setTop(mb);
		rootHost.setCenter(picPane);
	}

	private void addImgToSP(ImageView iViewPrev) {
		iViewPrev.setPreserveRatio(true);
		iViewPrev.setFitHeight(180);
		//iViewPrev.setFitWidth(100);
		pics.getChildren().add(picIndex , iViewPrev);
		picIndex++;
	}
	
}