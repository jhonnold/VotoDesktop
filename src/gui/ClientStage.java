package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import session.Client;
import session.Session;

public class ClientStage extends Stage{
	
	private Scene activeScene;
	private final Session session;
	
	// Subclass for the textarea which refreshes every second
	public class ClientArea extends TextArea implements ActionListener {
		
		private Timer t = new Timer(1000, this);
		
		/**
		 * Constructor that makes 20/6 textarea and starts timer
		 */
		public ClientArea() {
			setEditable(false);
			setPrefColumnCount(20);
			setPrefRowCount(6);
			t.start();
		}
		
		/**
		 * When timer fires, recall client list and write out to the textarea
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			this.clear();
			
			Platform.runLater(new Runnable() {
				public void run() {
					for (Client c : session.getClients()) {					
						appendText(c.getID() + "\n");
					}
				}
			});
		}
		
	}
	
	/**
	 * Constructor that takes session pointer
	 * @param s Reference back to the running session
	 */
	public ClientStage(Session s) {
		super();
		
		session = s;
		
		setTitle("Voto - Clients");
		
		TextArea clientList = new ClientArea();
		
		GridPane gp = new GridPane();
		gp.add(clientList,  0,  0);
		activeScene = new Scene(gp);
		
		setScene(activeScene);
		setResizable(false);
		sizeToScene(); //pack()
		show();
	} 
}
