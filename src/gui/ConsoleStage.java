package gui;

import java.io.PrintStream;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConsoleStage extends Stage {
	
	private Scene activeScene;
	private TextArea consoleOutput = new TextArea();
	
	public ConsoleStage() {
		super();
		
		setTitle("Voto - Console");
		
		consoleOutput.setPrefColumnCount(80);
		consoleOutput.setPrefRowCount(24);
		ConsoleOutput co = new ConsoleOutput(consoleOutput, "Voto-Desktop");
		
		GridPane gp = new GridPane();
		gp.add(consoleOutput,  0,  0);
		activeScene = new Scene(gp);
		
		setScene(activeScene);
		setResizable(false);	
		show();
		
		System.setOut(new PrintStream(co));
	}
	
	
}
