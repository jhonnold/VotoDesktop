package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The stage that will show connection info
 * @author Josh
 *
 */
public class ConnectionInfo extends Stage {
	
	int fontSize, height = 200, width = 400;
	
	/**
	 * The constructor to create the new stage
	 * @param n The Session Name
	 * @param parent The menubar that created this
	 */
	public ConnectionInfo(String n, VotoMenuBar parent) {
		super();
		
		Label lbl = new Label("IP: " + VotoDesktopFX.IP + "\nSession: " + n);
		fontSize = 40;
		lbl.setStyle(String.format("-fx-font-size: %dpx;", fontSize));
		BorderPane root = new BorderPane();
		root.setCenter(lbl);

		Scene scene = new Scene(root, width, height);

		setScene(scene);
		setTitle("VOTO - Connection Information");
		sizeToScene();
		setResizable(false);
		show();
		
		// Code from https://blog.idrsolutions.com/2012/11/adding-a-window-resize-listener-to-javafx-scene/
		/*scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				lbl.setStyle(String.format("-fx-font-size: %dpx;", fontSize += 40));
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				lbl.setStyle(String.format("-fx-font-size: %dpx;", fontSize += 40));
		    }
		});*/
		// End of code from https://blog.idrsolutions.com/2012/11/adding-a-window-resize-listener-to-javafx-scene/

		// On close reenable the close buttton in the menubar
		setOnCloseRequest(e -> parent.enableConnection());
	}
}