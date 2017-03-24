package gui;

import java.io.PrintStream;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConsoleStage extends Stage {
	
	private Scene activeScene;
	private TextArea consoleOutput = new TextArea();
	private VotoMenuBar parent;
	
	public ConsoleStage(VotoMenuBar parent) {
		super();
		
		this.parent = parent;
		
		setTitle("Voto - Console");
		
		consoleOutput.setPrefColumnCount(50);
		consoleOutput.setPrefRowCount(15);
		ConsoleOutput co = new ConsoleOutput(consoleOutput, "Voto-Desktop");
		System.setOut(new PrintStream(co));
		
		GridPane gp = new GridPane();
		gp.add(consoleOutput,  0,  0);
		activeScene = new Scene(gp);
		
		setScene(activeScene);
		setResizable(false);	
		sizeToScene();
		show();
		
		setOnCloseRequest(e -> parent.enableConsole());
	}
	
	
}
