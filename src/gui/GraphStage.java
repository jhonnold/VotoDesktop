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
	
	public GraphStage(Session s, VotoMenuBar parent, int id) {
		
		session = s;
		final VotoBarChart<String, Number> bc = new VotoBarChart<>(new CategoryAxis(), new NumberAxis(), id);
		
		bc.setTitle("Vote Summary for Question - " + id);
		
		Scene scene  = new Scene(bc);
		setScene(scene);
		show();
		
		setOnCloseRequest(e -> parent.enableGraph());
		
	}
	
	public GraphStage(Session s, VotoMenuBar parent) {
		session = s;
		
		//answerSet = session.getCurrentQuestion().getAnswerSet();
		//final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
		final VotoLiveBarChart<String, Number> bc = new VotoLiveBarChart<>(new CategoryAxis(), new NumberAxis());
		bc.setTitle("Vote Summary for Current Question");
		
		Scene scene  = new Scene(bc);
		setScene(scene);
		show();
		
		setOnCloseRequest(e -> parent.enableGraph());
	}
	
	private class VotoLiveBarChart<String, Number> extends BarChart<String, Number> implements ActionListener {
		
		private Timer t = new Timer(1000, this);
		
		public VotoLiveBarChart(Axis<String> xAxis, Axis<Number> yAxis) {
			super(xAxis, yAxis);
			
			setAnimated(false);
			setPrefSize(800, 600);
			t.start();
			
			XYChart.Series series = new XYChart.Series();
			
			//---------------------------------------------
			series.getData().add(new XYChart.Data("A", 0));
			series.getData().add(new XYChart.Data("B", 0));
			series.getData().add(new XYChart.Data("C", 0));
			series.getData().add(new XYChart.Data("D", 0));
			series.getData().add(new XYChart.Data("E", 0));
			//---------------------------------------------
			
			for (Vote v : session.returnQuestionData().keySet()) {
				
				Integer amount = session.returnQuestionData().get(v);
				
				series.getData().add(new XYChart.Data(v.getID(), amount));
			}
			
			getData().add(series);
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			XYChart.Series series = new XYChart.Series();
			
			//---------------------------------------------
			series.getData().add(new XYChart.Data("A", 0));
			series.getData().add(new XYChart.Data("B", 0));
			series.getData().add(new XYChart.Data("C", 0));
			series.getData().add(new XYChart.Data("D", 0));
			series.getData().add(new XYChart.Data("E", 0));
			//---------------------------------------------
			
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
			
			//---------------------------------------------
			series.getData().add(new XYChart.Data("A", 0));
			series.getData().add(new XYChart.Data("B", 0));
			series.getData().add(new XYChart.Data("C", 0));
			series.getData().add(new XYChart.Data("D", 0));
			series.getData().add(new XYChart.Data("E", 0));
			//---------------------------------------------
			
			for (Vote v : data.keySet()) {
				series.getData().add(new XYChart.Data(v.getID(), data.get(v)));
			}
			
			getData().add(series);
		}
			
	}
}
