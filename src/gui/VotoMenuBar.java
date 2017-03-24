package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import session.Session;

public class VotoMenuBar extends MenuBar {
	
	private Menu fileMenu, sessionMenu, windowMenu;
	private MenuItem newItem, openItem, saveItem, exitItem;
	private MenuItem nextItem;
	private MenuItem consoleItem, clientsItem, graphItem, connectionItem;
	private Session s;
	
	public VotoMenuBar(Session s) {
		this.s = s;
		
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
		//newItem.setOnAction(e -> new ConsoleStage());
		//openItem.setOnAction(e -> new ConsoleStage());
		//saveItem.setOnAction(e -> new ConsoleStage());
		//exitItem.setOnAction(e -> new ConsoleStage());
		//nextItem.setOnAction(e -> new ConsoleStage());
		consoleItem.setOnAction(e -> new ConsoleStage());
		clientsItem.setOnAction(e -> new ClientStage(s));
		//graphItem.setOnAction(e -> new GraphStage());
		//connectionItem.setOnAction(e -> new ConnectionStage());
	}
	
	public void setOpenFile(EventHandler<ActionEvent> value) {
		openItem.setOnAction(value);
		
	}
		
}
