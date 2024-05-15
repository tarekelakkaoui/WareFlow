package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class UsersPageController {

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	public void handleEmployeeButtonAction(ActionEvent event) {
		loadUI("/ca/mcgill/ecse/wareflow/javafx/fxml/pages/AddDeleteEmployee.fxml");
	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	public void handleGuestButtonAction(ActionEvent event) {
		loadUI("/ca/mcgill/ecse/wareflow/javafx/fxml/pages/AddDeleteGuest.fxml");
	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	public void handleManagerButtonAction(ActionEvent event) {
		loadUI("/ca/mcgill/ecse/wareflow/javafx/fxml/pages/UpdatedManager.fxml");
	}

	/**
	 * @author Mohamed Abdelhady
	 */
	private void loadUI(String fxmlPath) {
		try {
			URL resourceUrl = getClass().getResource(fxmlPath);
			if (resourceUrl == null) {
				System.err.println("Cannot load the FXML resource. Check if the path is correct: " + fxmlPath);
				return;
			}
			Parent root = FXMLLoader.load(resourceUrl);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("User Page");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load the FXML file for path: " + fxmlPath);
		}
	}
}
