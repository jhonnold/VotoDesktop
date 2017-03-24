package gui;

import java.io.PrintStream;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ConsoleStage extends Stage {
	
	private Scene activeScene;
	private TextArea consoleOutput = new TextArea();
	private VotoMenuBar parent;
	
	public ConsoleStage(VotoMenuBar mb) {
		super();
		
		setTitle("Voto - Console");
		
		mb.greyOutConsole();
		
		consoleOutput.setPrefColumnCount(50);
		consoleOutput.setPrefRowCount(15);
		ConsoleOutput co = new ConsoleOutput(consoleOutput, "Voto-Desktop");
		
		GridPane gp = new GridPane();
		gp.add(consoleOutput,  0,  0);
		activeScene = new Scene(gp);
		
		setScene(activeScene);
		setResizable(false);	
		sizeToScene();
		show();
		
		System.setOut(new PrintStream(co));
	}	
	
	@Override
	public void stop() {
		
		parent.enableConsole();
		super.close();
		
	}
	
}
