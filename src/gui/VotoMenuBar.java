package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import session.Session;

/**
 * The Main MenuBar Wrapper for the Voto Desktop Application
 * @author Josh
 *
 */
public class VotoMenuBar extends MenuBar {
	
	private Menu fileMenu, sessionMenu, windowMenu;
	private MenuItem newItem, openItem, saveItem, exitItem;
	private MenuItem nextItem;
	private MenuItem consoleItem, clientsItem, graphItem, connectionItem;
	private VotoDesktopFX parent;
	
	/**
	 * The universal menu bar for the program
	 * @param s The active session
	 * @param p The main stage 
	 */
	public VotoMenuBar(Session s, VotoDesktopFX p) {
		parent = p;
		
		fileMenu = new Menu("File");
		newItem = new MenuItem("New");
		openItem = new MenuItem("Open");
		saveItem = new MenuItem("Save");
		exitItem = new MenuItem("Exit");
		fileMenu.getItems().addAll(newItem, openItem, saveItem, exitItem);
		
		// Session menu
		sessionMenu = new Menu("Session");
		nextItem = new MenuItem("Next");
		sessionMenu.getItems().addAll(nextItem);
		
		// Window menu
		windowMenu = new Menu("Window");
		consoleItem = new MenuItem("Console");
		clientsItem = new MenuItem("Clients");
		graphItem = new MenuItem("Graph");
		connectionItem = new MenuItem("Connection Info");
		windowMenu.getItems().addAll(consoleItem, clientsItem, graphItem, connectionItem);
		
		getMenus().addAll(fileMenu, sessionMenu, windowMenu);
		
		// Set menu item actions
		newItem.setOnAction(e -> new SetupStage());
		openItem.setOnAction(e -> parent.hostGUI());
		//saveItem.setOnAction(e -> new ConsoleStage());
		exitItem.setOnAction(e -> parent.exitProgram());
		nextItem.setDisable(true);
		consoleItem.setOnAction(e -> { new ConsoleStage(this); consoleItem.setDisable(true); } );
		clientsItem.setOnAction(e -> { new ClientStage(s, this); clientsItem.setDisable(true); } );
		graphItem.setOnAction(e -> new GraphStage(s));
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
	 * Adjust what the next dropdown item does
	 * @param value The ActionEven to happen
	 */
	public void setNext(EventHandler<ActionEvent> value) {
		nextItem.setOnAction(value);
		nextItem.setDisable(false);
	}
}
