package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import session.Client;
import session.Session;

public class ClientStage extends Stage{
	
	private Scene activeScene;
	private final Session session;
	private VotoMenuBar parent;
	
	// Subclass for the textarea which refreshes every second
	public class ClientArea extends TextFlow implements ActionListener {
		
		private Timer t = new Timer(1000, this);
		
		/**
		 * Constructor that makes 20/6 textarea and starts timer
		 */
		public ClientArea() {
			//setEditable(false);
			//setPrefColumnCount(20);
			//setPrefRowCount(6);
			setPrefWidth(200);
			setPrefHeight(100);
			t.start();
		}
		
		/**
		 * When timer fires, recall client list and write out to the textarea
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//this.clear();
			
			Platform.runLater(new Runnable() {
				public void run() {
					
					getChildren().clear();
					
					for (Client c : session.getClients()) {					
						//appendText(c.getID() + "\n");
						Text t = new Text(c.getID() + " - " + 
										 (c.getLastVote() != null ? c.getLastVote().getID() : "") + "\n");
						if (c.getLastVote() != null) {
							t.setFill(Color.GREEN);
						} else {
							t.setFill(Color.RED);
						}
						getChildren().add(t);
					}
				}
			});
		}
		
	}
	
	/**
	 * Constructor that takes session pointer
	 * @param s Reference back to the running session
	 */
	public ClientStage(Session s, VotoMenuBar parent) {
		super();
		
		this.parent = parent;
		session = s;
		
		setTitle("VOTO");
		
		TextFlow clientList = new ClientArea();
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(clientList);
		activeScene = new Scene(sp);
		
		setScene(activeScene);
		setResizable(false);
		sizeToScene(); //pack()
		show();
		
		setOnCloseRequest(e -> parent.enableClient());
	} 
}
