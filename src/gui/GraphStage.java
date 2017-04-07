package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import session.Client;
import session.Session;
import session.Vote;

public class GraphStage extends Stage {
	
	private Session session;
	private HashMap<Vote, ArrayList<Client>> answerSet;
	private ArrayList<Integer> questionList;
	
	private BorderPane bp;
	private Button leftButton;
	private Button rightButton;
	
	private int cursor;
	
	public GraphStage(Session s, VotoMenuBar parent) {
		session = s;
		bp = new BorderPane();
		
		questionList = session.completedQuestionList();
		cursor = questionList.size();
		
		//answerSet = session.getCurrentQuestion().getAnswerSet();
		//final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
		final VotoLiveBarChart<String, Number> bc = new VotoLiveBarChart<>(new CategoryAxis(), new NumberAxis());
		bc.setTitle("Vote Summary");
		
		bp.setCenter(bc);
		
		leftButton = new Button("Previous");
		
		if (cursor <= 0) {
			leftButton.setDisable(true);
		}
		
		leftButton.setOnAction(e -> moveLeft());
		
		rightButton = new Button("Newer");
		
		if (cursor >= questionList.size()) {
			rightButton.setDisable(true);
		}
		
		rightButton.setOnAction(e -> moveRight());
		
		GridPane gp = new GridPane();
		ColumnConstraints c = new ColumnConstraints();
		c.setPercentWidth(33.3);
		gp.getColumnConstraints().addAll(c, c, c);
		
		// Add button to each HBox centered
		HBox b1 = new HBox();
		b1.setAlignment(Pos.CENTER);
		b1.getChildren().add(leftButton);
		HBox b2 = new HBox();
		b2.setAlignment(Pos.CENTER);
		b2.getChildren().add(rightButton);
		
		// Add the HBox's to the gridpane
		gp.add(b1, 0, 0);
		gp.add(b2, 2, 0);
		bp.setBottom(gp);
		
		Scene scene  = new Scene(bp);
		setScene(scene);
		show();
		
		setOnCloseRequest(e -> parent.enableGraph());
	}
	
	private void moveLeft() {
		
		rightButton.setDisable(false);
		bp.setCenter(new VotoBarChart(new CategoryAxis(), new NumberAxis(), questionList.get(--cursor)));
		
		if (cursor <= 0) {
			leftButton.setDisable(true);
		}
		
	}
	
	private void moveRight() {
		
		leftButton.setDisable(false);
		
		cursor++;
		
		if (cursor < questionList.size()) {
			bp.setCenter(new VotoBarChart(new CategoryAxis(), new NumberAxis(), questionList.get(cursor)));
		} else {
			
			bp.setCenter(new VotoLiveBarChart(new CategoryAxis(), new NumberAxis()));
			rightButton.setDisable(true);
		}
		
	}
	
	private class VotoLiveBarChart<String, Number> extends BarChart<String, Number> implements ActionListener {
		
		private Timer t = new Timer(1000, this);
		
		public VotoLiveBarChart(Axis<String> xAxis, Axis<Number> yAxis) {
			super(xAxis, yAxis);
			
			setAnimated(false);
			setPrefSize(800, 600);
			t.start();
			
			XYChart.Series series = new XYChart.Series();
			
			for (Vote v : session.returnQuestionData().keySet()) {
				
				Integer amount = session.returnQuestionData().get(v);
				
				series.getData().add(new XYChart.Data(v.getID(), amount));
			}
			
			getData().add(series);
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			XYChart.Series series = new XYChart.Series();
			
			for (Vote v : session.returnQuestionData().keySet()) {
				
				Integer amount = session.returnQuestionData().get(v);
				
				if (amount > 0) {
					series.getData().add(new XYChart.Data(v.getID(), amount));
				}
			}
			
			Platform.runLater(new Runnable() {
				public void run() {
					getData().set(0, series);
				}
			});
			
		}	
	}
	
	private class VotoBarChart<String, Number> extends BarChart<String, Number> {
		
		public VotoBarChart(Axis<String> xAxis, Axis<Number> yAxis, int imgID) {
			super(xAxis, yAxis);
			setPrefSize(800, 600);
			setAnimated(false);
			
			HashMap<Vote, Integer> data = session.returnQuestionData(imgID);
			XYChart.Series series = new XYChart.Series();
			
			for (Vote v : data.keySet()) {
				series.getData().add(new XYChart.Data(v.getID(), data.get(v)));
			}
			
			getData().add(series);
		}
			
	}
}
