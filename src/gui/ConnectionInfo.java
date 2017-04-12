package gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The stage that will show connection info
 * @author Josh
 *
 */
public class ConnectionInfo extends Stage {
	
	/**
	 * The constructor to create the new stage
	 * @param n The Session Name
	 * @param parent The menubar that created this
	 */
	public ConnectionInfo(String n, VotoMenuBar parent) {
		super();
		
		Label lbl = new Label("IP: " + VotoDesktopFX.IP + "\nSession: " + n);
		lbl.setStyle("-fx-font-size: 60px;");
		BorderPane root = new BorderPane();
		root.setCenter(lbl);

		Scene scene = new Scene(root, 600, 300);
		
		getIcons().add(new Image("file:ic_launcher.png"));
		
		setScene(scene);
		setTitle("VOTO - Connection Information");
		sizeToScene();
		setResizable(false);
		show();
		
		// On close reenable the close buttton in the menubar
		setOnCloseRequest(e -> parent.enableConnection());
	}
}