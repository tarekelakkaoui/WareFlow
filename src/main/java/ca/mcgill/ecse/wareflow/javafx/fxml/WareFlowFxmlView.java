package ca.mcgill.ecse.wareflow.javafx.fxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WareFlowFxmlView extends Application {

	public static final EventType<Event> REFRESH_EVENT = new EventType<>("REFRESH");
	private static WareFlowFxmlView instance;
	private List<Node> refreshableNodes = new ArrayList<>();

	/**
	 * @author Mohamed Abdelhady
	 */
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		try {
			var root = (Pane) FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			var scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);

			primaryStage.setTitle("WareFlow");
			primaryStage.show();
			refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Mohamed Abdelhady
	 * 
	 *         Register the node for receiving refresh events
	 */
	public void registerRefreshEvent(Node node) {
		refreshableNodes.add(node);
	}

	/**
	 * @author Mohamed Abdelhady
	 * 
	 *         Register multiple nodes for receiving refresh events
	 */
	public void registerRefreshEvent(Node... nodes) {
		for (var node : nodes) {
			refreshableNodes.add(node);
		}
	}

	/**
	 * @author Mohamed Abdelhady
	 * 
	 *         Remove the node from receiving refresh events
	 */
	public void removeRefreshableNode(Node node) {
		refreshableNodes.remove(node);
	}

	/**
	 * @author Mohamed Abdelhady
	 * 
	 *         Fire the refresh event to all registered nodes
	 */
	public void refresh() {
		for (Node node : refreshableNodes) {
			node.fireEvent(new Event(REFRESH_EVENT));
		}
	}

	/**
	 * @author Mohamed Abdelhady
	 * 
	 *         Gets instance of wareflow
	 */
	public static WareFlowFxmlView getInstance() {
		return instance;
	}

}
