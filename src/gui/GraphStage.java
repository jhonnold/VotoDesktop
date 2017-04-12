package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import session.Session;
import session.Vote;

/**
 * Graphically displays vote data with live updates
 * 
 * @author Nic/Jay
 *
 */
public class GraphStage extends Stage {

	private Session session;

	/**
	 * Create a Graph stage that takes in the ID for the question to be loaded.
	 * Since this polls for an ID, this will not be a live graph. It will load
	 * from a previous question and be static.
	 * 
	 * @param s
	 *            - The session to pull the data from.
	 * @param parent
	 *            - The MenuBar that created the Graph
	 * @param id
	 *            - The ID of the questiondata to be loaded
	 */
	public GraphStage(Session s, VotoMenuBar parent, int id) {

		session = s;
		final VotoBarChart<String, Number> bc = new VotoBarChart<>(new CategoryAxis(), new NumberAxis(), id);

		bc.setTitle("Vote Summary for Question - " + id);

		getIcons().add(new Image("file:ic_launcher.png"));
		
		Scene scene = new Scene(bc);
		setScene(scene);
		show();
		
		setOnCloseRequest(e -> parent.enableGraph());

	}

	/**
	 * Constructor that doesn't take in an ID. This will poll the current
	 * question from the given session and will update it every 1 second.
	 * 
	 * @param s - The Session for which to poll from.
	 * @param parent - The VotoMenuBar parent.
	 */
	public GraphStage(Session s, VotoMenuBar parent) {
		session = s;

		// answerSet = session.getCurrentQuestion().getAnswerSet();
		// final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
		final VotoLiveBarChart<String, Number> bc = new VotoLiveBarChart<>(new CategoryAxis(), new NumberAxis());
		bc.setTitle("Vote Summary for Current Question");

		getIcons().add(new Image("file:ic_launcher.png"));
		
		Scene scene = new Scene(bc);
		setScene(scene);
		show();

		setOnCloseRequest(e -> parent.enableGraph());
	}

	/**
	 * Class that is for the currentQuestion in the Session class. It will poll
	 * every 1 second to update the graph in a "live" fashion.
	 * @param <String>
	 * @param <Number>
	 */
	private class VotoLiveBarChart<String, Number> extends BarChart<String, Number> implements ActionListener {

		private Timer t = new Timer(1000, this);

		public VotoLiveBarChart(Axis<String> xAxis, Axis<Number> yAxis) {
			super(xAxis, yAxis);

			setAnimated(false);
			setPrefSize(800, 600);
			t.start();

			XYChart.Series series = new XYChart.Series();

			// ---------------------------------------------
			series.getData().add(new XYChart.Data("A", 0));
			series.getData().add(new XYChart.Data("B", 0));
			series.getData().add(new XYChart.Data("C", 0));
			series.getData().add(new XYChart.Data("D", 0));
			series.getData().add(new XYChart.Data("E", 0));
			// ---------------------------------------------

			for (Vote v : session.returnQuestionData().keySet()) {

				Integer amount = session.returnQuestionData().get(v);

				series.getData().add(new XYChart.Data(v.getID(), amount));
			}

			getData().add(series);

		}

		// Update graph with new data
		@Override
		public void actionPerformed(ActionEvent arg0) {

			XYChart.Series series = new XYChart.Series();

			// ---------------------------------------------
			series.getData().add(new XYChart.Data("A", 0));
			series.getData().add(new XYChart.Data("B", 0));
			series.getData().add(new XYChart.Data("C", 0));
			series.getData().add(new XYChart.Data("D", 0));
			series.getData().add(new XYChart.Data("E", 0));
			// ---------------------------------------------

			for (Vote v : session.returnQuestionData().keySet()) {

				Integer amount = session.returnQuestionData().get(v);

				if (amount > 0) {
					series.getData().add(new XYChart.Data(v.getID(), amount));
				}
			}
			
			// Do the Java-FX calls in time with the Java-FX thread.
			Platform.runLater(new Runnable() {
				public void run() {
					getData().set(0, series);
				}
			});

		}
	}
	
	/**
	 * The non-live bar chart that simply draws once to the scene
	 * based on the image ID that it is given.
	 * @param <String>
	 * @param <Number>
	 */
	private class VotoBarChart<String, Number> extends BarChart<String, Number> {

		public VotoBarChart(Axis<String> xAxis, Axis<Number> yAxis, int imgID) {
			super(xAxis, yAxis);
			setPrefSize(800, 600);
			setAnimated(false);

			HashMap<Vote, Integer> data = session.returnQuestionData(imgID);
			XYChart.Series series = new XYChart.Series();

			// ---------------------------------------------
			series.getData().add(new XYChart.Data("A", 0));
			series.getData().add(new XYChart.Data("B", 0));
			series.getData().add(new XYChart.Data("C", 0));
			series.getData().add(new XYChart.Data("D", 0));
			series.getData().add(new XYChart.Data("E", 0));
			// ---------------------------------------------

			for (Vote v : data.keySet()) {
				series.getData().add(new XYChart.Data(v.getID(), data.get(v)));
			}

			getData().add(series);
		}

	}
}
