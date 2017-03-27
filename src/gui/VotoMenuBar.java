package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
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
	private VotoDesktopFX parent;
	
	public VotoMenuBar(Session s, VotoDesktopFX p) {
		this.s = s;
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
		//newItem.setOnAction(e -> new ConsoleStage());
		//openItem.setOnAction(e -> new ConsoleStage());
		//saveItem.setOnAction(e -> new ConsoleStage());
		exitItem.setOnAction(e -> parent.exitProgram());
		//nextItem.setOnAction(e -> new ConsoleStage());
		consoleItem.setOnAction(e -> { new ConsoleStage(this); consoleItem.setDisable(true); } );
		clientsItem.setOnAction(e -> { new ClientStage(s, this); clientsItem.setDisable(true); } );
		//graphItem.setOnAction(e -> new GraphStage());
		connectionItem.setOnAction(e -> new ConnectionInfo(s.ID));
	}
	
	public void setOpenFile(EventHandler<ActionEvent> value) {
		openItem.setOnAction(value);
	}

	public void enableConsole() {
		consoleItem.setDisable(false);
	}
	
	public void enableClient() {
		clientsItem.setDisable(false);
	}
	
	public void setNext(EventHandler<ActionEvent> value) {
		nextItem.setOnAction(value);
	}
}
