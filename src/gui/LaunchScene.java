package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

public class LaunchScene extends Scene {
	
	private static BorderPane container = new BorderPane();
	private Button hostButton, setupButton;
	
	public LaunchScene(int width, int height, VotoMenuBar mb) {
		super(container, width, height);

		container.setTop(mb);
		
		//Instantiate elements
		hostButton = new Button("Host Session");
		hostButton.setOnAction(e -> VotoDesktopFX.hostGUI());
		
		setupButton = new Button("Setup Session");
		setupButton.setOnAction(e -> new SetupStage());
		
		// Create 3x1 Grid pane 100% for row, 33.33% for each column
				GridPane gp = new GridPane();
				ColumnConstraints c = new ColumnConstraints();
				RowConstraints r = new RowConstraints();
				c.setPercentWidth(50);
				r.setPercentHeight(100);
				gp.getColumnConstraints().addAll(c, c);
				gp.getRowConstraints().add(r);
				
				// Add button to each HBox centered
				HBox b1 = new HBox();
				b1.setAlignment(Pos.CENTER);
				b1.getChildren().add(hostButton);
				HBox b2 = new HBox();
				b2.setAlignment(Pos.CENTER);
				b2.getChildren().add(setupButton);
				
				// Add the HBox's to the gridpane
				gp.add(b1, 0, 0);
				gp.add(b2, 1, 0);
				container.setCenter(gp);
		
	}
	
	public static void reinstallMenuBar(VotoMenuBar mb) {
		container.setTop(mb);
	}
}
