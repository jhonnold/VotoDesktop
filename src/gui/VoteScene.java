package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class VoteScene extends Scene {

	private GridPane joinGrid;
	private Button[] votingButtons;
	private static BorderPane rootJoin = new BorderPane();

	public VoteScene(double width, double height) {
		super(rootJoin, width, height);

		FlowPane center = new FlowPane();

		//Temporary background in place of image
		center.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(1), new Insets(0, 0, 0, 0))));

		// Instantiate new GUI items
		joinGrid = new GridPane();
		joinGrid.setHgap(20);

		int numButtons = 4;
		votingButtons = new Button[numButtons];
		for (int i = 0; i < numButtons; i++) {
			votingButtons[i] = new Button(Character.toString((char) (0x0041 +i)));
			votingButtons[i].setPrefSize(70, 20);
			joinGrid.add(votingButtons[i], i, 0);
		}

		// Set borders
		FlowPane up = new FlowPane();
		up.setPrefHeight(30);
		FlowPane left = new FlowPane();
		left.setPrefWidth(30);
		FlowPane right = new FlowPane();
		right.setPrefWidth(30);
		FlowPane down = new FlowPane(5,5);
		down.setPrefHeight(30);
		down.setAlignment(Pos.CENTER);
		down.getChildren().add(joinGrid);
		FlowPane.setMargin(joinGrid, new Insets(5,5,5,5));
		rootJoin.setBottom(down);
		rootJoin.setCenter(center);
		rootJoin.setTop(up);
		rootJoin.setLeft(left);
		rootJoin.setRight(right);
	}

}
