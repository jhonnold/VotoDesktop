package gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ConnectionInfo extends Stage {
	
	public ConnectionInfo(String n) {
		super();
		
		Label lbl = new Label("IP: " + VotoDesktopFX.IP + "\nSession: " + n);
		lbl.setStyle("-fx-font-size: 40px;");
		BorderPane root = new BorderPane();
		root.setCenter(lbl);
		
		Scene scene = new Scene(root, 400, 200);
		
		setScene(scene);
		setTitle("VOTO - Connection Information");
		sizeToScene();
		setResizable(false);
		show();
	}
}