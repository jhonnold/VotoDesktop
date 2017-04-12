package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import session.Client;
import session.Session;

/**
 * The stage that displays the list of connected clients.
 * When the client is RED they haven't voted for this question.
 * If the cleint is GREEN it will list their vote after their ID.
 * @author Jay
 */
public class ClientStage extends Stage{
	
	// The scene
	private Scene activeScene;
	// The active session to pull client list from
	private final Session session;
	// The menu bar from which this is made
	private VotoMenuBar parent;
	
	/**
	 * TextFlow that will be used to display the updated client list
	 * Uses a 1 second timer to constantly recall the list and redraw
	 */
	public class ClientArea extends TextFlow implements ActionListener {
		
		private Timer t = new Timer(1000, this);
		
		/**
		 * Constructor that makes 200/100 Textflow and starts timer
		 */
		public ClientArea() {
			setPrefWidth(200);
			setPrefHeight(100);
			t.start();
		}
		
		/**
		 * When timer fires, recall client list and write out to the textarea.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//this.clear();
			
			// Run the redraw on the Java-FX Thread
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
		
		getIcons().add(new Image("file:ic_launcher.png"));
		
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
