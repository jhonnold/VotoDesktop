package gui;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import session.Session;

/**
 * The Main MenuBar Wrapper for the Voto Desktop Application
 * @author Josh
 *
 */
public class VotoMenuBar extends MenuBar {
	
	private Menu fileMenu, sessionMenu, windowMenu, graphItem;
	private MenuItem newItem, openItem, saveItem, exitItem;
	private MenuItem nextItem, stopItem;
	private MenuItem consoleItem, clientsItem, connectionItem;
	
	private ArrayList<Integer> completedQuestion;
	private Session session;
	
	/**
	 * The universal menu bar for the program
	 * @param s The active session
	 * @param p The main stage 
	 */
	public VotoMenuBar(Session s) {
		
		completedQuestion = s.completedQuestionList();
		session = s;
		
		// File menu
		fileMenu = new Menu("_File");
		newItem = new MenuItem("_New");
		newItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
		openItem = new MenuItem("_Open");
		openItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
		//saveItem = new MenuItem("Save");
		exitItem = new MenuItem("E_xit");
		fileMenu.getItems().addAll(newItem, openItem, exitItem);
		
		// Session menu
		sessionMenu = new Menu("_Session");
		nextItem = new MenuItem("_Next");
		stopItem = new MenuItem("_Stop");
		sessionMenu.getItems().addAll(nextItem, stopItem);
		
		// Window menu
		windowMenu = new Menu("_Window");
		consoleItem = new MenuItem("_Console");
		clientsItem = new MenuItem("C_lients");
		graphItem = new Menu("_Graph");
		
		MenuItem cQues = new MenuItem("Current");
		cQues.setOnAction(e -> new GraphStage(session, this));
		graphItem.getItems().add(cQues);
		
		for (Integer id : completedQuestion) {
			MenuItem temp = new MenuItem("" + id);
			temp.setOnAction(e -> new GraphStage(session, this, id));
			graphItem.getItems().add(temp);
		}
		
		
		connectionItem = new MenuItem("Connection _Info");
		windowMenu.getItems().addAll(consoleItem, clientsItem, graphItem, connectionItem);
		
		getMenus().addAll(fileMenu, sessionMenu, windowMenu);
		
		// Set menu item actions
		newItem.setOnAction(e -> new SetupStage());
		openItem.setOnAction(e -> {newItem.setDisable(true); openItem.setDisable(true); VotoDesktopFX.hostGUI(); } );
		//saveItem.setOnAction(e -> new ConsoleStage());
		exitItem.setOnAction(e -> VotoDesktopFX.exitProgram());
		nextItem.setDisable(true);
		stopItem.setDisable(true);
		consoleItem.setOnAction(e -> { new ConsoleStage(this); consoleItem.setDisable(true); } );
		clientsItem.setOnAction(e -> { new ClientStage(s, this); clientsItem.setDisable(true); } );
		connectionItem.setOnAction(e -> { new ConnectionInfo(s.ID, this); connectionItem.setDisable(true); });
	}
	
	/**
	 * Change open to do something else
	 * @param value The new ActionEvent
	 */
	public void setOpenFile(EventHandler<ActionEvent> value) {
		openItem.setOnAction(value);
	}
	
	/**
	 * Re-enable the console dropdown item
	 */
	public void enableConsole() {
		consoleItem.setDisable(false);
	}
	
	/**
	 * Re-enable the client dropdown item
	 */
	public void enableClient() {
		clientsItem.setDisable(false);
	}
	
	/**
	 * Re-enable the connection dropdown item
	 */
	public void enableConnection() {
		connectionItem.setDisable(false);
	}
	
	/**
	 * Re-enable the graph dropdown item
	 */
	public void enableGraph() {
		graphItem.setDisable(false);
	}
	
	/**
	 * Adjust what the next dropdown item does
	 * @param value The ActionEven to happen
	 */
	public void setSession(EventHandler<ActionEvent> nextEvent, EventHandler<ActionEvent> stopEvent) {
		nextItem.setOnAction(nextEvent);
		nextItem.setDisable(false);
		stopItem.setOnAction(stopEvent);
		stopItem.setDisable(false);
	}
	
	public void menuBarInLaunch() {
		
		nextItem.setDisable(true);
		stopItem.setDisable(true);
		
		
		
	}
	
	public void setNext(String s) {
		nextItem.setText(s);
	}
	
	public void updateQuestionList() {
		completedQuestion = session.completedQuestionList();
		
		graphItem.getItems().clear();
		
		MenuItem cQues = new MenuItem("Current");
		cQues.setOnAction(e -> new GraphStage(session, this));
		graphItem.getItems().add(cQues);
		
		for (Integer id : completedQuestion) {
			MenuItem temp = new MenuItem("" + id);
			temp.setOnAction(e -> new GraphStage(session, this, id));
			graphItem.getItems().add(temp);
		}
	}
}
