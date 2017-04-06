package gui;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import session.Client;
import session.Session;
import session.Vote;

public class GraphStage extends Stage {
	
	private Session session;
	private HashMap<Vote, ArrayList<Client>> answerSet;

	public GraphStage(Session s) {
		session = s;
		
		//answerSet = session.getCurrentQuestion().getAnswerSet();
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
		bc.setTitle("Vote Summary");
		xAxis.setLabel("Vote");       
		yAxis.setLabel("Amount");

		XYChart.Series series1 = new XYChart.Series();
		HashMap<Vote, Integer> data = session.returnQuestionData();
		
		for (Vote v : data.keySet()) {
			series1.getData().add(new XYChart.Data(v.getID(), data.get(v)));
		}
		//series1.getData().add(new XYChart.Data("A", 3)); //answerSet.get("A").size()));
		//series1.getData().add(new XYChart.Data("B", 1)); //answerSet.get("B").size()));
		//series1.getData().add(new XYChart.Data("C", 3)); //answerSet.get("C").size()));
		//series1.getData().add(new XYChart.Data("D", 2)); //answerSet.get("D").size()));
		//series1.getData().add(new XYChart.Data("E", 0)); //answerSet.get("E").size()));

		Scene scene  = new Scene(bc, 800, 600);
		bc.getData().add(series1);
		setScene(scene);
		show();
	}
}
